import java.io.*;

import java.net.*;

import java.util.Scanner;
import java.net.InetSocketAddress;
import java.net.InetAddress;




public class CBC{

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

byte[] temp = new byte[16];



int i;

stream.read(ciphertext);

int a = (int) ciphertext[0];

if(a>=10)	a=a-10;



FileOutputStream out = new FileOutputStream(file);
	
        String key = "oiEAitVbJHJFdkjW";



	
byte[] IV2 = new byte[16];

	for(i=0;i<16;i++){

	IV2[i] = 'A';}

	AES b = new AES();

byte[] rp =
new byte[16];

if(a==0){

while(stream.read(ciphertext)==16){


 

	
        rp = b.decrypt(ciphertext, key.getBytes() );

	int[] p = new int[16];

	int[] IV = new int[16];
	for(i=0;i<16;i++)
	
p[i]=(int) rp[i];

	
for(i=0;i<16;i++)
	
IV[i]=(int) IV2[i];


for(i=0;i<16;i++)
	p[i]=p[i]^IV[i];

for(i=0;i<16;i++){

rp[i]=(byte) p[i];

IV2[i]=(byte) IV[i];}
	


	out.write(rp);
for(i=0;i<16;i++)
IV2[i]=ciphertext[i];
}
}
else{
stream.read(ciphertext);


while(stream.read(temp)==16){




        rp = b.decrypt(ciphertext, key.getBytes() );

	int[] p = new int[16];

int[] IV = new int[16];
	for(i=0;i<16;i++)
	
p[i]=(int) rp[i];

	
for(i=0;i<16;i++)
	
IV[i]=(int) IV2[i];


for(i=0;i<16;i++)
	p[i]=p[i]^IV[i];

for(i=0;i<16;i++){

rp[i]=(byte) p[i];

IV2[i]=(byte) IV[i];}
	


	out.write(rp);

for(i=0;i<16;i++)
IV2[i]=ciphertext[i];
for(i=0;i<16;i++)	ciphertext[i] = temp[i];
}
	





       rp = b.decrypt(temp, key.getBytes() );


	int[] p = new int[16];

	int[] IV = new int[16];
	for(i=0;i<16;i++)
	
p[i]=(int) rp[i];

	
for(i=0;i<16;i++)
	
IV[i]=(int) IV2[i];


for(i=0;i<16;i++)
	p[i]=p[i]^IV[i];

for(i=0;i<16;i++){

rp[i]=(byte) p[i];

IV2[i]=(byte) IV[i];}
	

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


byte[] IV1 = new byte[16];



int i;
	for(i=0;i<16;i++){

	IV1[i] = 'A';}


	
byte[] num = new byte[16];

num[0] = (byte) (in.available()%16);


for(i=1;i<16;i++)	num[i] = '0';
writer.write(num);





if((in.available())%16==1){

while(in.available()!=1){

	in.read(plaintext);



int[] plain = new int[16];

	for(i=0;i<16;i++)
	
plain[i]=(int) plaintext[i];

	int[] IV = new int[16];

	for(i=0;i<16;i++)
	
IV[i]=(int) IV1[i];

for(i=0;i<16;i++)
	plain[i]=plain[i]^IV[i];

for(i=0;i<16;i++){
plaintext[i]=(byte) plain[i];

IV1[i]=(byte) IV[i];}
    


        byte[] ciphertext = a.encrypt( plaintext, key.getBytes() );



            writer.write(ciphertext);

for(i=0;i<16;i++)

IV1[i]=ciphertext[i];



}}
else{

while((in.available())>16){

	in.read(plaintext);


int[] plain = new int[16];

	for(i=0;i<16;i++)
	
plain[i]=(int) plaintext[i];

	int[] IV = new int[16];

	for(i=0;i<16;i++)
	
IV[i]=(int) IV1[i];

for(i=0;i<16;i++)
	plain[i]=plain[i]^IV[i];

for(i=0;i<16;i++){
plaintext[i]=(byte) plain[i];

IV1[i]=(byte) IV[i];}
    
        byte[] ciphertext = a.encrypt( plaintext, key.getBytes() );

	writer.write(ciphertext);

for(i=0;i<16;i++)

IV1[i]=ciphertext[i];




}

	int k = in.available();

	in.read(plaintext);


   


	for(i=16;i>k;i--){

	plaintext[i-1] = '0';}




int[] plain = new int[16];

	for(i=0;i<16;i++)
	
plain[i]=(int) plaintext[i];

	int[] IV = new int[16];

	for(i=0;i<16;i++)
	
IV[i]=(int) IV1[i];

for(i=0;i<16;i++)
	plain[i]=plain[i]^IV[i];

for(i=0;i<16;i++){
plaintext[i]=(byte) plain[i];

IV1[i]=(byte) IV[i];}
 
        byte[] ciphertext = a.encrypt( plaintext, key.getBytes() );

	writer.write(ciphertext);


for(i=0;i<16;i++)

IV1[i]=ciphertext[i];


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