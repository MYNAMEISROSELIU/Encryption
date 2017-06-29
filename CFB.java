import java.io.*;

import java.net.*;

import java.util.Scanner;
import java.net.InetSocketAddress;
import java.net.InetAddress;




public class CFB {

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

byte[] ciphertext = new byte[8];

byte[] contin = new byte[8];



int i;
	byte[] IV1 = new byte[16];



	byte[] temp = new byte[16];

for(i=0;i<16;i++)	
IV1[i] = 'A';

stream.read(ciphertext);

int a = (int) ciphertext[0];

if(a>=10)	a=a-10;

System.out.println(a);

FileOutputStream out = new FileOutputStream(file);
	
        String key = "oiEAitVbJHJFdkjW";



	AES b = new AES();
if(a==0){

while(stream.read(ciphertext)==8){


int[] temp1 = new int[8];
for(i=0;i<8;i++)	temp1[i] = (byte) ciphertext[i]; 

      temp = b.encrypt( IV1, key.getBytes() );
int[] temp2 = new int[8];

for(i=0;i<8;i++)
	temp2[i] =(int) temp[i];
	
for(i=0;i<8;i++)	
temp2[i]=temp2[i]^temp1[i];

byte[] rp = new byte[8]; 

for(i=0;i<8;i++)	
rp[i] = (byte) temp2[i];

for(i=0;i<8;i++)	
ciphertext[i]=(byte) temp1[i];


	out.write(rp);
for(i=8;i<16;i++)
IV1[i]=ciphertext[i-8];
}
}
else{
stream.read(ciphertext);


while(stream.read(contin)==8){




int[] temp1 = new int[8];
for(i=0;i<8;i++)	temp1[i] = (byte) ciphertext[i]; 

      temp = b.encrypt( IV1, key.getBytes() );
int[] temp2 = new int[8];

for(i=0;i<8;i++)
	temp2[i] =(int) temp[i];
	
for(i=0;i<8;i++)	
temp2[i]=temp2[i]^temp1[i];

byte[] rp = new byte[8]; 

for(i=0;i<8;i++)	
rp[i] = (byte) temp2[i];

for(i=0;i<8;i++)	
ciphertext[i]=(byte) temp1[i];


	out.write(rp);
for(i=8;i<16;i++)
IV1[i]=ciphertext[i-8];
for(i=0;i<8;i++)	ciphertext[i] = contin[i];
}
	





        temp = b.encrypt( IV1, key.getBytes() );


int[] temp1 = new int[8];
for(i=0;i<8;i++)	temp1[i] = (byte) contin[i]; 

      temp = b.encrypt( IV1, key.getBytes() );
int[] temp2 = new int[8];

for(i=0;i<8;i++)
	temp2[i] =(int) temp[i];
	
for(i=0;i<8;i++)	
temp2[i]=temp2[i]^temp1[i];

byte[] rp = new byte[8]; 

for(i=0;i<8;i++)	
rp[i] = (byte) temp2[i];

for(i=0;i<8;i++)	
contin[i]=(byte) temp1[i];



for(i=8;i<16;i++)
IV1[i]=contin[i-8];




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
	byte[] plaintext = new byte[8];

	
        String key = "oiEAitVbJHJFdkjW";


        AES a = new AES();



	byte[] IV1 = new byte[16];



	byte[] temp = new byte[16];
int i;
for(i=0;i<16;i++)	
IV1[i] = 'A';


byte[] ciphertext = new byte[8];




	
byte[] num = new byte[8];

num[0] = (byte) (in.available()%8);


for(i=1;i<8;i++)	num[i] = '0';
writer.write(num);





if((in.available())%8==1){

while(in.available()!=1){


       temp = a.encrypt( IV1, key.getBytes() );


	in.read(plaintext);

	int[] plain = new int[8];

	for(i=0;i<8;i++)
	
plain[i]=(int) plaintext[i];

	int[] temp1 = new int[8];

	for(i=0;i<8;i++)

	temp1[i]=(int) temp[i];


for(i=0;i<8;i++)	
temp1[i]=plain[i]^temp1[i];

for(i=0;i<8;i++){

ciphertext[i]=(byte) temp1[i];}




            writer.write(ciphertext);
for(i=8;i<16;i++)
IV1[i]=ciphertext[i-8];



}}
else{

while((in.available())>8){

	       temp = a.encrypt( IV1, key.getBytes() );


	in.read(plaintext);

	int[] plain = new int[8];

	for(i=0;i<8;i++)
	
plain[i]=(int) plaintext[i];

	int[] temp1 = new int[8];

	for(i=0;i<8;i++)

	temp1[i]=(int) temp[i];


for(i=0;i<8;i++)	
temp1[i]=plain[i]^temp1[i];

for(i=0;i<8;i++){

ciphertext[i]=(byte) temp1[i];}


	writer.write(ciphertext);

for(i=8;i<16;i++)
IV1[i]=ciphertext[i-8];



}

	int k = in.available();

	in.read(plaintext);

	for(i=8;i>k;i--){

	plaintext[i-1] = '0';}


       temp = a.encrypt( IV1, key.getBytes() );



	int[] plain = new int[8];

	for(i=0;i<8;i++)
	
plain[i]=(int) plaintext[i];

	int[] temp1 = new int[8];

	for(i=0;i<8;i++)

	temp1[i]=(int) temp[i];


for(i=0;i<8;i++)	
temp1[i]=plain[i]^temp1[i];

for(i=0;i<8;i++){

ciphertext[i]=(byte) temp1[i];}








 

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