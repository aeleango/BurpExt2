
import org.json.JSONObject;
import org.xerial.snappy.Snappy;

import java.io.*;
import java.util.Base64;

public class LogParser {
    private BufferedWriter bw ;
    public void extrator(String infile,String outfile) throws Exception{
        InputStreamReader ir = new InputStreamReader(new FileInputStream(infile));
        this.bw = new BufferedWriter(new FileWriter(outfile));
        String line = null;
        int ch=-1;
        StringBuffer sb = new StringBuffer();
        while((ch=ir.read())!=-1){
            if(((char)ch)==','){
                line = sb.toString().trim();
                try {
                    parseLine(line);
                    sb = new StringBuffer();
                }catch (Exception e){
                    System.out.println("解释数据失败: "+line);
                    e.printStackTrace();
                    System.exit(1);
                }
            }else{
                sb.append((char)ch);
            }
        }

    }

    private  void parseLine(String line) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(line);
        bytes = Snappy.uncompress(bytes);
        String data = new String(bytes);
        JSONObject json = new JSONObject(data);
//        System.out.println(json.toString(2));
//        System.exit(1);
//            System.out.println(json.toString(2));
        bw.write(String.format("%s %s %s", json.getString("method"), json.getString("url"), json.getString("responseHeaders")));
        bw.flush();
    }

}