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
    	System.out.print("�������Ӱ�Ķ���ID��");
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
        //�������
        int loc=json.indexOf(".jpg");
        System.out.println("������ַ��https://img3.doubanio.com/view/photo/m/public/"+json.substring(loc-11,loc+4));
        
        //���Ӣ����
        System.out.println();
        System.out.print("��Ƭ��������");
        loc=json.indexOf("\"original_title\": ");
        tmp=json.substring(loc+19);
        int loc_end=tmp.indexOf("\", \"summary\": \"");
        tmp=tmp.substring(0, loc_end);
        System.out.println(tmp);
        
        //�������
        System.out.print("���֡�������");
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
        
        //������
        System.out.print("���ꡡ������");
        loc=json.indexOf("\"year\": ");
        tmp=json.substring(loc+9);
        loc_end=tmp.indexOf("\", \"images\": ");
        tmp=tmp.substring(0, loc_end);
        System.out.println(tmp);
        
        //�������
        System.out.print("��������ء�");
        loc=json.indexOf("\"countries\": ");
        tmp=json.substring(loc+15);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end-1);
        tmp=tmp.replace("\", \"", "/");
        System.out.println(tmp);
        
        //������
        System.out.print("���ࡡ����");
        loc=json.indexOf("\"genres\": ");
        tmp=json.substring(loc+12);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end-1);
        tmp=tmp.replace("\", \"", "/");
        System.out.println(tmp);
        
        //����api
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
        
        //�������
        System.out.print("������ԡ�");
        loc=json.indexOf("\"language\": ");
        tmp=json.substring(loc+14);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end-1);
        tmp=tmp.replace("\", \"", "/");
        System.out.println(tmp);
        
        //�����ӳ����
        System.out.print("����ӳ���ڡ�");
        loc=json.indexOf("\"pubdate\": ");
        tmp=json.substring(loc+13);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end-1);
        tmp=tmp.replace("\", \"", "/");
        System.out.println(tmp);
        
        //����
    	paChong douban = new paChong("https://movie.douban.com/subject/"+ID+"/", "UTF-8");
        String IMDb;
		IMDb = douban.run();
        paChong imdb= new paChong("http://www.imdb.com/title/"+IMDb, "UTF-8");
        String IMDb_rate=imdb.run();
        //���IMDb����
        System.out.print("��IMDb����  ");
        System.out.println(IMDb_rate.replace(" based on ", "/10 from ")+" users");
        
        //���IMDb����
        System.out.print("��IMDb����  ");
        System.out.println("http://www.imdb.com/title/"+IMDb+"/");
        
        //�����������
        System.out.print("�򶹰����֡�");
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
        
        //�����������
        System.out.print("�򶹰����ӡ�");
        System.out.println("https://movie.douban.com/subject/"+ID+"/");
        
        //����Ǿ缯���������
        loc=json.indexOf("\"episodes\": ");
        if(loc!=-1) {
        	System.out.print("�򼯡�������");
        	tmp=json.substring(loc+14);
        	loc_end=tmp.indexOf("]");
        	tmp=tmp.substring(0, loc_end-1);
            tmp=tmp.replace("\", \"", "/");
            System.out.println(tmp);
        }
        
        //���Ƭ��
        System.out.print("��Ƭ��������");
        loc=json.indexOf("\"movie_duration\": ");
        tmp=json.substring(loc+20);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end-1);
        tmp=tmp.replace("\", \"", "/");
        System.out.println(tmp);
        
        //�������
        System.out.print("�򵼡����ݡ�");
        loc=json.indexOf("\"director\": ");
        tmp=json.substring(loc+14);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end-1);
        tmp=tmp.replace("\", \"", "/");
        for(int i=0;i<tmp.length();i++){
        	if(tmp.charAt(i)=='/'){
        		System.out.println();
        		System.out.print("������������");
        	}
        	else
        		System.out.print(tmp.charAt(i));
        }
		System.out.println();
        
        //�������
        System.out.print("���������ݡ�");
        loc=json.indexOf("\"cast\": ");
        tmp=json.substring(loc+10);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end-1);
        tmp=tmp.replace("\", \"", "/");
        for(int i=0;i<tmp.length();i++){
        	if(tmp.charAt(i)=='/'){
        		System.out.println();
        		System.out.print("������������");
        	}
        	else
        		System.out.print(tmp.charAt(i));
        }
		System.out.println();
        
        //������
        System.out.println();
        System.out.println("��򡡡���");
        loc=json.indexOf("\"summary\": ");
        tmp=json.substring(loc+12);
        loc_end=tmp.indexOf("\", \"attrs\": ");
        tmp=tmp.substring(0, loc_end);
        System.out.println("����"+tmp);
        
        //�����ǩ
        System.out.println();
        System.out.print("��ꡡ��ǩ��");
        loc=json.indexOf("\"tags\": ");
        tmp=json.substring(loc+8);
        loc_end=tmp.indexOf("]");
        tmp=tmp.substring(0, loc_end+1);
        loc=tmp.indexOf("\"name\": ");
        loc_end=tmp.indexOf("\"}");
        System.out.print(tmp.substring(loc+9,loc_end));
        tmp=tmp.substring(loc_end+2);
        while(tmp.charAt(0)==','){
        	System.out.print("��");
            loc=tmp.indexOf("\"name\": ");
            loc_end=tmp.indexOf("\"}");
            System.out.print(tmp.substring(loc+9,loc_end));
            tmp=tmp.substring(loc_end+2);
        }
        
        System.out.println();
        System.out.println();
        System.out.println("�������Ϣ��");
        System.out.println();
        System.out.println("����Ƶ��ͼ��");
        System.out.println();

    }

    public static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream(),"utf-8"));//��ֹ����
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
    
  //Unicodeת����  
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
           char letter = (char) Integer.parseInt(charStr, 16); // 16����parse�����ַ�����     
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

        URL url = new URL(u);// �������ӣ��ַ�����ʽ��������һ��URL����

        HttpURLConnection urlConnection = (HttpURLConnection) url
                .openConnection();// ��URL

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream(), encoding));// �õ������������������ҳ������
        String line; // ��ȡ�����������ݣ�����ʾ
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