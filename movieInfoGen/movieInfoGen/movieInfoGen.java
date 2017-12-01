package movieInfoGen;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
//26357945
//24750534
//27000999
public class movieInfoGen {

    public static void main(String[] args) throws Exception {
    	
    	Scanner in=new Scanner(System.in);
    	System.out.print("请输入电影的豆瓣ID：");
    	String ID=in.nextLine();
        in.close();
    	String tmp;
    	String url="https://api.douban.com/v2/movie/subject/"+ID;
        String json = loadJSON(url);
        int len=json.length();
        for(int i=0;i<len-8;i++){
        	if(json.charAt(i)=='\\' && json.charAt(i+1)=='u'){
        		tmp=decodeUnicode(json.substring(i, i+6));
        		json=json.substring(0, i)+tmp+json.substring(i+6, len);
        		len=len-5;
        	}
        }
        //输出海报
        int loc=json.indexOf(".jpg");
        System.out.println("海报地址：https://img3.doubanio.com/view/photo/m/public/"+json.substring(loc-11,loc+4));
        
        //输出英文名
        System.out.println();
        System.out.print("◎片　　名　");
        loc=json.indexOf("\"original_title\": ");
        tmp=json.substring(loc+19);
        int loc_end=tmp.indexOf("\", \"summary\": \"");
        tmp=tmp.substring(0, loc_end);
        System.out.println(tmp);
        
        //输出又名
        System.out.print("◎又　　名　");
        loc=json.indexOf("\"aka\": ");
        tmp=json.substring(loc+9);
        loc_end=tmp.indexOf("]");
        if(loc_end==-1){
        	loc=json.indexOf("\"title\": ");
            tmp=json.substring(loc+10);
            loc_end=tmp.indexOf("\"");
            tmp=tmp.substring(0, loc_end);
        }
        else{
	        tmp=tmp.substring(0, loc_end-1);
	        tmp=tmp.replace("\", \"", "/");
	        tmp=tmp.replace("\\", "");
        }
	    System.out.println(tmp);
        
        //输出年代
        System.out.print("◎年　　代　");
        loc=json.indexOf("\"year\": ");
        tmp=json.substring(loc+9);
        loc_end=tmp.indexOf("\", \"images\": ");
        tmp=tmp.substring(0, loc_end);
        System.out.println(tmp);
        
        //输出产地
        System.out.print("◎产　　地　");
        loc=json.indexOf("\"countries\": ");
        tmp=json.substring(loc+15);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end-1);
        tmp=tmp.replace("\", \"", "/");
        System.out.println(tmp);
        
        //输出类别
        System.out.print("◎类　　别　");
        loc=json.indexOf("\"genres\": ");
        tmp=json.substring(loc+12);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end-1);
        tmp=tmp.replace("\", \"", "/");
        System.out.println(tmp);
        
        //更换api
        url="https://api.douban.com/v2/movie/"+ID;
        json = loadJSON(url);
        len=json.length();
        for(int i=0;i<len-9;i++){
        	if(json.charAt(i)=='\\' && json.charAt(i+1)=='u'){
        		tmp=decodeUnicode(json.substring(i, i+6));
        		json=json.substring(0, i)+tmp+json.substring(i+6, len);
        		len=len-5;
        	}
        }
        
        //输出语言
        System.out.print("◎语　　言　");
        loc=json.indexOf("\"language\": ");
        tmp=json.substring(loc+14);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end-1);
        tmp=tmp.replace("\", \"", "/");
        System.out.println(tmp);
        
        //输出上映日期
        System.out.print("◎上映日期　");
        loc=json.indexOf("\"pubdate\": ");
        tmp=json.substring(loc+13);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end-1);
        tmp=tmp.replace("\", \"", "/");
        System.out.println(tmp);
        
        //爬虫
    	paChong douban = new paChong("https://movie.douban.com/subject/"+ID+"/", "UTF-8");
        String IMDb;
		IMDb = douban.run();
        paChong imdb= new paChong("http://www.imdb.com/title/"+IMDb, "UTF-8");
        String IMDb_rate=imdb.run();
        //输出IMDb评分
        System.out.print("◎IMDb评分  ");
        System.out.println(IMDb_rate.replace(" based on ", "/10 from ")+" users");
        
        //输出IMDb链接
        System.out.print("◎IMDb链接  ");
        System.out.println("http://www.imdb.com/title/"+IMDb+"/");
        
        //输出豆瓣评分
        System.out.print("◎豆瓣评分　");
        loc=json.indexOf("\"average\": ");
        tmp=json.substring(loc+12);
        loc_end=tmp.indexOf(", \"min\": ");
        tmp=tmp.substring(0, loc_end);
        tmp=tmp.replace("\", \"numRaters\": ", "/10 from ")+" users";
        loc=tmp.indexOf(" users")-4;
        while(tmp.charAt(loc)>='0'&&tmp.charAt(loc)<='9'){
        	tmp=tmp.substring(0,loc+1)+","+tmp.substring(loc+1);
        	loc=loc-4;
        }
        System.out.println(tmp);
        
        //输出豆瓣链接
        System.out.print("◎豆瓣链接　");
        System.out.println("https://movie.douban.com/subject/"+ID+"/");
        
        //如果是剧集，输出集数
        loc=json.indexOf("\"episodes\": ");
        if(loc!=-1) {
        	System.out.print("◎集　　数　");
        	tmp=json.substring(loc+14);
        	loc_end=tmp.indexOf("]");
        	tmp=tmp.substring(0, loc_end-1);
            tmp=tmp.replace("\", \"", "/");
            System.out.println(tmp);
        }
        
        //输出片长
        System.out.print("◎片　　长　");
        loc=json.indexOf("\"movie_duration\": ");
        tmp=json.substring(loc+20);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end-1);
        tmp=tmp.replace("\", \"", "/");
        System.out.println(tmp);
        
        //输出导演
        System.out.print("◎导　　演　");
        loc=json.indexOf("\"director\": ");
        tmp=json.substring(loc+14);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end-1);
        tmp=tmp.replace("\", \"", "/");
        for(int i=0;i<tmp.length();i++){
        	if(tmp.charAt(i)=='/'){
        		System.out.println();
        		System.out.print("　　　　　　");
        	}
        	else
        		System.out.print(tmp.charAt(i));
        }
		System.out.println();
        
        //输出主演
        System.out.print("◎主　　演　");
        loc=json.indexOf("\"cast\": ");
        tmp=json.substring(loc+10);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end-1);
        tmp=tmp.replace("\", \"", "/");
        for(int i=0;i<tmp.length();i++){
        	if(tmp.charAt(i)=='/'){
        		System.out.println();
        		System.out.print("　　　　　　");
        	}
        	else
        		System.out.print(tmp.charAt(i));
        }
		System.out.println();
        
        //输出简介
        System.out.println();
        System.out.println("◎简　　介");
        loc=json.indexOf("\"summary\": ");
        tmp=json.substring(loc+12);
        loc_end=tmp.indexOf("\", \"attrs\": ");
        tmp=tmp.substring(0, loc_end);
        System.out.println("　　"+tmp);
        
        //输出标签
        System.out.println();
        System.out.print("◎标　　签　");
        loc=json.indexOf("\"tags\": ");
        tmp=json.substring(loc+8);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end+1);
        loc=tmp.indexOf("\"name\": ");
        loc_end=tmp.indexOf("\"}");
        System.out.print(tmp.substring(loc+9,loc_end));
        tmp=tmp.substring(loc_end+2);
        while(tmp.charAt(0)==','){
        	System.out.print("，");
            loc=tmp.indexOf("\"name\": ");
            loc_end=tmp.indexOf("\"}");
            System.out.print(tmp.substring(loc+9,loc_end));
            tmp=tmp.substring(loc_end+2);
        }
        
        System.out.println();
        System.out.println();
        System.out.println("◎编码信息　");
        System.out.println();
        System.out.println("◎视频截图　");
        System.out.println();

    }

    public static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream(),"utf-8"));//防止乱码
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return json.toString();
    }
    
  //Unicode转中文  
    public static String decodeUnicode(final String dataStr) {     
       int start = 0;     
       int end = 0;     
       final StringBuffer buffer = new StringBuffer();     
       while (start > -1) {     
           end = dataStr.indexOf("\\u", start + 2);     
           String charStr = "";     
           if (end == -1) {     
               charStr = dataStr.substring(start + 2, dataStr.length());     
           } else {     
               charStr = dataStr.substring(start + 2, end);     
           }     
           char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。     
           buffer.append(new Character(letter).toString());     
           start = end;     
       }     
       return buffer.toString();     
    }
}

class paChong {
    private String u;
    private String encoding;

    public paChong(String u, String encoding) {
        this.u = u;
        this.encoding = encoding;
    }

    public String run() throws Exception {

        URL url = new URL(u);// 根据链接（字符串格式），生成一个URL对象

        HttpURLConnection urlConnection = (HttpURLConnection) url
                .openConnection();// 打开URL

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream(), encoding));// 得到输入流，即获得了网页的内容
        String line; // 读取输入流的数据，并显示
        int loc;
        while ((line = reader.readLine()) != null) {
        	if(this.u.indexOf("douban")!=-1){
	        	loc=line.indexOf("http://www.imdb.com/title/tt");
	        	if(loc!=-1)
	        		return line.substring(loc+26,loc+35);
        	}
        	else{
	        	loc=line.indexOf("<strong title=");
	        	if(loc!=-1){
	        		return line.substring(loc+15,line.indexOf(" user ratings"));
	        	}
        	}
        }
        return "";
    }
}