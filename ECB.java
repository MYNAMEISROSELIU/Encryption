import java.io.*;

import java.net.*;

import java.util.Scanner;
import java.net.InetSocketAddress;
import java.net.InetAddress;




public class ECB {

    public static void main( String argv[]){

 
        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Server / 2. Client\n");
  
        int opt = scanner.nextInt();

        if( opt==1 ){

            Server s = new Server();

        }else if(opt==2){

            Client c = new Client();

        }

    }


}


class Server {

    ServerSocket s;

    Server(){

        Scanner scanner = new Scanner(System.in);
System.out.print("Enter the filename:");

String filename = scanner.nextLine();


        System.out.print("Enter the port: ");

        int p = scanner.nextInt();

        go(p,filename);

        
    }

    void go(int port,String file){

        try{

            s = new ServerSocket(port);


            while(true){

                Socket sock = s.accept();


                InputStream stream = sock.getInputStream();

byte[] ciphertext = new byte[16];

byte[] contin = new byte[16];



int i;

stream.read(ciphertext);

int a = (int) ciphertext[0];

if(a>=10)	a=a-10;



FileOutputStream out = new FileOutputStream(file);
	
        String key = "oiEAitVbJHJFdkjW";



	AES b = new AES();

byte[] rp =
new byte[16];

if(a==0){

while(stream.read(ciphertext)==16){


 

	
        rp = b.decrypt(ciphertext, key.getBytes() );

	out.write(rp);

}
}
else{
stream.read(ciphertext);


while(stream.read(contin)==16){




        rp = b.decrypt(ciphertext, key.getBytes() );

	out.write(rp);


for(i=0;i<16;i++)	ciphertext[i] = contin[i];
}
	





       rp = b.decrypt(contin, key.getBytes() );


	byte[] qq = new byte[a];

	for(i=0;i<a;i++){

	qq[i] = rp[i];}

	out.write(qq);
}


sock.close();





}  }	catch(FileNotFoundException e){
	System.out.println("The file is non-exist!\n");

	System.exit(-1);}    
catch (IOException e){

            e.printStackTrace();

        }

    }


}

class Client {

    Socket s;

    OutputStream writer;

    String key;

    Client(){

        Scanner scanner = new Scanner(System.in);

System.out.print("Enter the filename:");

String filename = scanner.nextLine();




        System.out.print("Enter the IP: ");

        String tip = scanner.nextLine();

        System.out.print("Enter the port: ");

        int tport = scanner.nextInt();
	

     
        try {

    Socket socket = new Socket();         
InetSocketAddress isa = new InetSocketAddress(tip,tport);
socket.connect(isa, tport);
writer = socket.getOutputStream();
	FileInputStream in = new FileInputStream(filename);
	byte[] plaintext = new byte[16];

	
        String key = "oiEAitVbJHJFdkjW";


        AES a = new AES();









int i;


	
byte[] num = new byte[16];

num[0] = (byte) (in.available()%16);


for(i=1;i<16;i++)	num[i] = '0';
writer.write(num);





if((in.available())%16==1){

while(in.available()!=1){

	in.read(plaintext);

	System.out.println(in.available());


        byte[] ciphertext = a.encrypt( plaintext, key.getBytes() );



            writer.write(ciphertext);


}}
else{

while((in.available())>16){

	in.read(plaintext);


        byte[] ciphertext = a.encrypt( plaintext, key.getBytes() );

	writer.write(ciphertext);




}

	int k = in.available();

	in.read(plaintext);

	for(i=16;i>k;i--){

	plaintext[i-1] = '0';}



        byte[] ciphertext = a.encrypt( plaintext, key.getBytes() );

	writer.write(ciphertext);





}

socket.close();







        }
	catch(FileNotFoundException e){
	System.out.println("The file is non-exist!\n");

	System.exit(-1);}
	 catch( IOException e ){

            e.printStackTrace();

        }

    }

    void connect(String ip, int port) throws IOException{

        s = new Socket(ip, port);

    }

    void write(String input) throws IOException{

        writer = s.getOutputStream();

        for(int i=0; i<input.length(); i++){

            writer.write( (int)input.charAt(i) );

        }

        writer.write(-1);

    }

    void close() throws IOException{

        writer.close();

    }

}