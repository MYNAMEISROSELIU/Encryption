import java.util.regex.*;
import java.io.*;

class IllegalInputException extends RuntimeException{
    public IllegalInputException (String s){
        super(s);
    }
}

public class AES{

    String[][] roundKey;
    final private String[] Rcon = new String [] {"00","01","02","04","08","10","20","40","80","1B","36"};
    final private String[][] S_BOX = new String[][] {
    {"63","7C","77","7B","F2","6B","6F","C5","30","01","67","2B","FE","D7","AB","76"},
    {"CA","82","C9","7D","FA","59","47","F0","AD","D4","A2","AF","9C","A4","72","C0"},
    {"B7","FD","93","26","36","3F","F7","CC","34","A5","E5","F1","71","D8","31","15"},
    {"04","C7","23","C3","18","96","05","9A","07","12","80","E2","EB","27","B2","75"},
    {"09","83","2C","1A","1B","6E","5A","A0","52","3B","D6","B3","29","E3","2F","84"},
    {"53","D1","00","ED","20","FC","B1","5B","6A","CB","BE","39","4A","4C","58","CF"},
    {"D0","EF","AA","FB","43","4D","33","85","45","F9","02","7F","50","3C","9F","A8"},
    {"51","A3","40","8F","92","9D","38","F5","BC","B6","DA","21","10","FF","F3","D2"},
    {"CD","0C","13","EC","5F","97","44","17","C4","A7","7E","3D","64","5D","19","73"},
    {"60","81","4F","DC","22","2A","90","88","46","EE","B8","14","DE","5E","0B","DB"},
    {"E0","32","3A","0A","49","06","24","5C","C2","D3","AC","62","91","95","E4","79"},
    {"E7","C8","37","6D","8D","D5","4E","A9","6C","56","F4","EA","65","7A","AE","08"},
    {"BA","78","25","2E","1C","A6","B4","C6","E8","DD","74","1F","4B","BD","8B","8A"},
    {"70","3E","B5","66","48","03","F6","0E","61","35","57","B9","86","C1","1D","9E"},
    {"E1","F8","98","11","69","D9","8E","94","9B","1E","87","E9","CE","55","28","DF"},
    {"8C","A1","89","0D","BF","E6","42","68","41","99","2D","0F","B0","54","BB","16"}
} ;
    final private String[][] I_S_BOX = new String[][] {
    {"52","09","6A","D5","30","36","A5","38","BF","40","A3","9E","81","F3","D7","FB"},
    {"7C","E3","39","82","9B","2F","FF","87","34","8E","43","44","C4","DE","E9","CB"},
    {"54","7B","94","32","A6","C2","23","3D","EE","4C","95","0B","42","FA","C3","4E"},
    {"08","2E","A1","66","28","D9","24","B2","76","5B","A2","49","6D","8B","D1","25"},
    {"72","F8","F6","64","86","68","98","16","D4","A4","5C","CC","5D","65","B6","92"},
    {"6C","70","48","50","FD","ED","B9","DA","5E","15","46","57","A7","8D","9D","84"},
    {"90","D8","AB","00","8C","BC","D3","0A","F7","E4","58","05","B8","B3","45","06"},
    {"D0","2C","1E","8F","CA","3F","0F","02","C1","AF","BD","03","01","13","8A","6B"},
    {"3A","91","11","41","4F","67","DC","EA","97","F2","CF","CE","F0","B4","E6","73"},
    {"96","AC","74","22","E7","AD","35","85","E2","F9","37","E8","1C","75","DF","6E"},
    {"47","F1","1A","71","1D","29","C5","89","6F","B7","62","0E","AA","18","BE","1B"},
    {"FC","56","3E","4B","C6","D2","79","20","9A","DB","C0","FE","78","CD","5A","F4"},
    {"1F","DD","A8","33","88","07","C7","31","B1","12","10","59","27","80","EC","5F"},
    {"60","51","7F","A9","19","B5","4A","0D","2D","E5","7A","9F","93","C9","9C","EF"},
    {"A0","E0","3B","4D","AE","2A","F5","B0","C8","EB","BB","3C","83","53","99","61"},
    {"17","2B","04","7E","BA","77","D6","26","E1","69","14","63","55","21","0C","7D"}
    };

    final private int[] mul9 = new int[] {
0x00,0x09,0x12,0x1b,0x24,0x2d,0x36,0x3f,0x48,0x41,0x5a,0x53,0x6c,0x65,0x7e,0x77,
0x90,0x99,0x82,0x8b,0xb4,0xbd,0xa6,0xaf,0xd8,0xd1,0xca,0xc3,0xfc,0xf5,0xee,0xe7,
0x3b,0x32,0x29,0x20,0x1f,0x16,0x0d,0x04,0x73,0x7a,0x61,0x68,0x57,0x5e,0x45,0x4c,
0xab,0xa2,0xb9,0xb0,0x8f,0x86,0x9d,0x94,0xe3,0xea,0xf1,0xf8,0xc7,0xce,0xd5,0xdc,
0x76,0x7f,0x64,0x6d,0x52,0x5b,0x40,0x49,0x3e,0x37,0x2c,0x25,0x1a,0x13,0x08,0x01,
0xe6,0xef,0xf4,0xfd,0xc2,0xcb,0xd0,0xd9,0xae,0xa7,0xbc,0xb5,0x8a,0x83,0x98,0x91,
0x4d,0x44,0x5f,0x56,0x69,0x60,0x7b,0x72,0x05,0x0c,0x17,0x1e,0x21,0x28,0x33,0x3a,
0xdd,0xd4,0xcf,0xc6,0xf9,0xf0,0xeb,0xe2,0x95,0x9c,0x87,0x8e,0xb1,0xb8,0xa3,0xaa,
0xec,0xe5,0xfe,0xf7,0xc8,0xc1,0xda,0xd3,0xa4,0xad,0xb6,0xbf,0x80,0x89,0x92,0x9b,
0x7c,0x75,0x6e,0x67,0x58,0x51,0x4a,0x43,0x34,0x3d,0x26,0x2f,0x10,0x19,0x02,0x0b,
0xd7,0xde,0xc5,0xcc,0xf3,0xfa,0xe1,0xe8,0x9f,0x96,0x8d,0x84,0xbb,0xb2,0xa9,0xa0,
0x47,0x4e,0x55,0x5c,0x63,0x6a,0x71,0x78,0x0f,0x06,0x1d,0x14,0x2b,0x22,0x39,0x30,
0x9a,0x93,0x88,0x81,0xbe,0xb7,0xac,0xa5,0xd2,0xdb,0xc0,0xc9,0xf6,0xff,0xe4,0xed,
0x0a,0x03,0x18,0x11,0x2e,0x27,0x3c,0x35,0x42,0x4b,0x50,0x59,0x66,0x6f,0x74,0x7d,
0xa1,0xa8,0xb3,0xba,0x85,0x8c,0x97,0x9e,0xe9,0xe0,0xfb,0xf2,0xcd,0xc4,0xdf,0xd6,
0x31,0x38,0x23,0x2a,0x15,0x1c,0x07,0x0e,0x79,0x70,0x6b,0x62,0x5d,0x54,0x4f,0x46};
    final private int[] mul11 = new int[] {
0x00,0x0b,0x16,0x1d,0x2c,0x27,0x3a,0x31,0x58,0x53,0x4e,0x45,0x74,0x7f,0x62,0x69,
0xb0,0xbb,0xa6,0xad,0x9c,0x97,0x8a,0x81,0xe8,0xe3,0xfe,0xf5,0xc4,0xcf,0xd2,0xd9,
0x7b,0x70,0x6d,0x66,0x57,0x5c,0x41,0x4a,0x23,0x28,0x35,0x3e,0x0f,0x04,0x19,0x12,
0xcb,0xc0,0xdd,0xd6,0xe7,0xec,0xf1,0xfa,0x93,0x98,0x85,0x8e,0xbf,0xb4,0xa9,0xa2,
0xf6,0xfd,0xe0,0xeb,0xda,0xd1,0xcc,0xc7,0xae,0xa5,0xb8,0xb3,0x82,0x89,0x94,0x9f,
0x46,0x4d,0x50,0x5b,0x6a,0x61,0x7c,0x77,0x1e,0x15,0x08,0x03,0x32,0x39,0x24,0x2f,
0x8d,0x86,0x9b,0x90,0xa1,0xaa,0xb7,0xbc,0xd5,0xde,0xc3,0xc8,0xf9,0xf2,0xef,0xe4,
0x3d,0x36,0x2b,0x20,0x11,0x1a,0x07,0x0c,0x65,0x6e,0x73,0x78,0x49,0x42,0x5f,0x54,
0xf7,0xfc,0xe1,0xea,0xdb,0xd0,0xcd,0xc6,0xaf,0xa4,0xb9,0xb2,0x83,0x88,0x95,0x9e,
0x47,0x4c,0x51,0x5a,0x6b,0x60,0x7d,0x76,0x1f,0x14,0x09,0x02,0x33,0x38,0x25,0x2e,
0x8c,0x87,0x9a,0x91,0xa0,0xab,0xb6,0xbd,0xd4,0xdf,0xc2,0xc9,0xf8,0xf3,0xee,0xe5,
0x3c,0x37,0x2a,0x21,0x10,0x1b,0x06,0x0d,0x64,0x6f,0x72,0x79,0x48,0x43,0x5e,0x55,
0x01,0x0a,0x17,0x1c,0x2d,0x26,0x3b,0x30,0x59,0x52,0x4f,0x44,0x75,0x7e,0x63,0x68,
0xb1,0xba,0xa7,0xac,0x9d,0x96,0x8b,0x80,0xe9,0xe2,0xff,0xf4,0xc5,0xce,0xd3,0xd8,
0x7a,0x71,0x6c,0x67,0x56,0x5d,0x40,0x4b,0x22,0x29,0x34,0x3f,0x0e,0x05,0x18,0x13,
0xca,0xc1,0xdc,0xd7,0xe6,0xed,0xf0,0xfb,0x92,0x99,0x84,0x8f,0xbe,0xb5,0xa8,0xa3};
    final private int[] mul13 = new int[] {
0x00,0x0d,0x1a,0x17,0x34,0x39,0x2e,0x23,0x68,0x65,0x72,0x7f,0x5c,0x51,0x46,0x4b,
0xd0,0xdd,0xca,0xc7,0xe4,0xe9,0xfe,0xf3,0xb8,0xb5,0xa2,0xaf,0x8c,0x81,0x96,0x9b,
0xbb,0xb6,0xa1,0xac,0x8f,0x82,0x95,0x98,0xd3,0xde,0xc9,0xc4,0xe7,0xea,0xfd,0xf0,
0x6b,0x66,0x71,0x7c,0x5f,0x52,0x45,0x48,0x03,0x0e,0x19,0x14,0x37,0x3a,0x2d,0x20,
0x6d,0x60,0x77,0x7a,0x59,0x54,0x43,0x4e,0x05,0x08,0x1f,0x12,0x31,0x3c,0x2b,0x26,
0xbd,0xb0,0xa7,0xaa,0x89,0x84,0x93,0x9e,0xd5,0xd8,0xcf,0xc2,0xe1,0xec,0xfb,0xf6,
0xd6,0xdb,0xcc,0xc1,0xe2,0xef,0xf8,0xf5,0xbe,0xb3,0xa4,0xa9,0x8a,0x87,0x90,0x9d,
0x06,0x0b,0x1c,0x11,0x32,0x3f,0x28,0x25,0x6e,0x63,0x74,0x79,0x5a,0x57,0x40,0x4d,
0xda,0xd7,0xc0,0xcd,0xee,0xe3,0xf4,0xf9,0xb2,0xbf,0xa8,0xa5,0x86,0x8b,0x9c,0x91,
0x0a,0x07,0x10,0x1d,0x3e,0x33,0x24,0x29,0x62,0x6f,0x78,0x75,0x56,0x5b,0x4c,0x41,
0x61,0x6c,0x7b,0x76,0x55,0x58,0x4f,0x42,0x09,0x04,0x13,0x1e,0x3d,0x30,0x27,0x2a,
0xb1,0xbc,0xab,0xa6,0x85,0x88,0x9f,0x92,0xd9,0xd4,0xc3,0xce,0xed,0xe0,0xf7,0xfa,
0xb7,0xba,0xad,0xa0,0x83,0x8e,0x99,0x94,0xdf,0xd2,0xc5,0xc8,0xeb,0xe6,0xf1,0xfc,
0x67,0x6a,0x7d,0x70,0x53,0x5e,0x49,0x44,0x0f,0x02,0x15,0x18,0x3b,0x36,0x21,0x2c,
0x0c,0x01,0x16,0x1b,0x38,0x35,0x22,0x2f,0x64,0x69,0x7e,0x73,0x50,0x5d,0x4a,0x47,
0xdc,0xd1,0xc6,0xcb,0xe8,0xe5,0xf2,0xff,0xb4,0xb9,0xae,0xa3,0x80,0x8d,0x9a,0x97
    };
    final private int[] mul14 = new int[] {
0x00,0x0e,0x1c,0x12,0x38,0x36,0x24,0x2a,0x70,0x7e,0x6c,0x62,0x48,0x46,0x54,0x5a,
0xe0,0xee,0xfc,0xf2,0xd8,0xd6,0xc4,0xca,0x90,0x9e,0x8c,0x82,0xa8,0xa6,0xb4,0xba,
0xdb,0xd5,0xc7,0xc9,0xe3,0xed,0xff,0xf1,0xab,0xa5,0xb7,0xb9,0x93,0x9d,0x8f,0x81,
0x3b,0x35,0x27,0x29,0x03,0x0d,0x1f,0x11,0x4b,0x45,0x57,0x59,0x73,0x7d,0x6f,0x61,
0xad,0xa3,0xb1,0xbf,0x95,0x9b,0x89,0x87,0xdd,0xd3,0xc1,0xcf,0xe5,0xeb,0xf9,0xf7,
0x4d,0x43,0x51,0x5f,0x75,0x7b,0x69,0x67,0x3d,0x33,0x21,0x2f,0x05,0x0b,0x19,0x17,
0x76,0x78,0x6a,0x64,0x4e,0x40,0x52,0x5c,0x06,0x08,0x1a,0x14,0x3e,0x30,0x22,0x2c,
0x96,0x98,0x8a,0x84,0xae,0xa0,0xb2,0xbc,0xe6,0xe8,0xfa,0xf4,0xde,0xd0,0xc2,0xcc,
0x41,0x4f,0x5d,0x53,0x79,0x77,0x65,0x6b,0x31,0x3f,0x2d,0x23,0x09,0x07,0x15,0x1b,
0xa1,0xaf,0xbd,0xb3,0x99,0x97,0x85,0x8b,0xd1,0xdf,0xcd,0xc3,0xe9,0xe7,0xf5,0xfb,
0x9a,0x94,0x86,0x88,0xa2,0xac,0xbe,0xb0,0xea,0xe4,0xf6,0xf8,0xd2,0xdc,0xce,0xc0,
0x7a,0x74,0x66,0x68,0x42,0x4c,0x5e,0x50,0x0a,0x04,0x16,0x18,0x32,0x3c,0x2e,0x20,
0xec,0xe2,0xf0,0xfe,0xd4,0xda,0xc8,0xc6,0x9c,0x92,0x80,0x8e,0xa4,0xaa,0xb8,0xb6,
0x0c,0x02,0x10,0x1e,0x34,0x3a,0x28,0x26,0x7c,0x72,0x60,0x6e,0x44,0x4a,0x58,0x56,
0x37,0x39,0x2b,0x25,0x0f,0x01,0x13,0x1d,0x47,0x49,0x5b,0x55,0x7f,0x71,0x63,0x6d,
0xd7,0xd9,0xcb,0xc5,0xef,0xe1,0xf3,0xfd,0xa7,0xa9,0xbb,0xb5,0x9f,0x91,0x83,0x8d
    };
    private int round;

    public AES(){
        roundKey = new String[11][16];
    }
//
// ######## ##    ##  ######  ########  ##    ## ########  ######## 
// ##       ###   ## ##    ## ##     ##  ##  ##  ##     ##    ##    
// ##       ####  ## ##       ##     ##   ####   ##     ##    ##    
// ######   ## ## ## ##       ########     ##    ########     ##    
// ##       ##  #### ##       ##   ##      ##    ##           ##    
// ##       ##   ### ##    ## ##    ##     ##    ##           ##    
// ######## ##    ##  ######  ##     ##    ##    ##           ##    
    public String[] encrypt(String[] plain, String[] cipherkey){
        //Check input
        isValidVector(plain);
        isValidVector(cipherkey);
        round = 0;

        KeyExpansion( cipherkey );

        String[] t = AddRoundKey(plain);
        round++;

        while(round<=9){
            //System.out.printf("Round %d\n", round);
            t = SubBytes(t);
            //AES.printState(t);
            t = ShiftRows(t);
            //AES.printState(t);
            t = MixColumns(t);
            //AES.printState(t);
            t = AddRoundKey(t);
            //AES.printState(t);
            round++;
        }
        t = SubBytes(t);
        //AES.printState(t);
        t = ShiftRows(t);
        //AES.printState(t);
        t = AddRoundKey(t);
        //AES.printState(t);
        return t;
    }
    
//
// ########  ########  ######  ########  ##    ## ########  ######## 
// ##     ## ##       ##    ## ##     ##  ##  ##  ##     ##    ##    
// ##     ## ##       ##       ##     ##   ####   ##     ##    ##    
// ##     ## ######   ##       ########     ##    ########     ##    
// ##     ## ##       ##       ##   ##      ##    ##           ##    
// ##     ## ##       ##    ## ##    ##     ##    ##           ##    
// ########  ########  ######  ##     ##    ##    ##           ##    
    public String[] decrypt(String[] cipher, String[] cipherkey){
        //Check input
        isValidVector(cipher);
        isValidVector(cipherkey);
        round = 10;

        KeyExpansion( cipherkey );

        String[] t = AddRoundKey(cipher);
        round--;

        while(round>=1){
            //System.out.printf("Round %d\n", round);
            t = invShiftRows(t);
            //AES.printState(t);
            t = invSubBytes(t);
            //AES.printState(t);
            t = AddRoundKey(t);
            //AES.printState(t);
            t = invMixColumns(t);
            //AES.printState(t);
            round--;
        }
        t = invShiftRows(t);
        //AES.printState(t);
        t = invSubBytes(t);
        //AES.printState(t);
        t = AddRoundKey(t);
        //AES.printState(t);
        return t;
    };

//
//  ######  ##     ## ########  ########  ##    ## ######## ########  ######  
// ##    ## ##     ## ##     ## ##     ##  ##  ##     ##    ##       ##    ## 
// ##       ##     ## ##     ## ##     ##   ####      ##    ##       ##       
//  ######  ##     ## ########  ########     ##       ##    ######    ######  
//       ## ##     ## ##     ## ##     ##    ##       ##    ##             ## 
// ##    ## ##     ## ##     ## ##     ##    ##       ##    ##       ##    ## 
//  ######   #######  ########  ########     ##       ##    ########  ######  

    String[] SubBytes(String[] input){
        isValidVector(input);
        String [] r = new String[ input.length ];
        for(int i=0; i<input.length; i++){
            r[i] = S_BOX[Integer.parseInt(input[i].substring(0,1), 16)][Integer.parseInt(input[i].substring(1,2), 16)];
        }
        return r;
    }
    String[] invSubBytes(String[] input){
        isValidVector(input);
        String [] r = new String[ input.length ];
        for(int i=0; i<input.length; i++){
            r[i] = I_S_BOX[Integer.parseInt(input[i].substring(0,1), 16)][Integer.parseInt(input[i].substring(1,2), 16)];
        }
        return r;
    }

//
//  ######  ##     ## #### ######## ######## ########  
// ##    ## ##     ##  ##  ##          ##    ##     ## 
// ##       ##     ##  ##  ##          ##    ##     ## 
//  ######  #########  ##  ######      ##    ########  
//       ## ##     ##  ##  ##          ##    ##   ##   
// ##    ## ##     ##  ##  ##          ##    ##    ##  
//  ######  ##     ## #### ##          ##    ##     ## 

    String[] ShiftRows(String[] input){
        isValidVector(input);
        String[] r = new String[ input.length ];
        /*/reorder
        0  4 8  12
        5  9 13 1
        10 14 2 6 
        15 3 7 11 
        */
        r = new String[] {
            input[0], input[5], input[10], input[15], input[4], input[9], input[14], input[3],
            input[8], input[13], input[2], input[7], input[12], input[1], input[6], input[11] 
        };
        return r;
    }
    String[] invShiftRows(String[] input){
        isValidVector(input);
        String[] r = new String[ input.length ];
        /*/reorder
        0  4  8  12
        13 1  5  9
        10 14 2  6 
        7  11 15 3 
        */
        r = new String[] {
            input[0], input[13], input[10], input[7], input[4], input[1], input[14], input[11],
            input[8], input[5], input[2], input[15], input[12], input[9], input[6], input[3] 
        };
        return r;
    }

//
// ##     ## #### ##     ##  ######   #######  ##       
// ###   ###  ##   ##   ##  ##    ## ##     ## ##       
// #### ####  ##    ## ##   ##       ##     ## ##       
// ## ### ##  ##     ###    ##       ##     ## ##       
// ##     ##  ##    ## ##   ##       ##     ## ##       
// ##     ##  ##   ##   ##  ##    ## ##     ## ##       
// ##     ## #### ##     ##  ######   #######  ######## 

    String[] MixColumns(String[] input){
        isValidVector(input);
        
        String[] r = new String[input.length];
        for(int i=0; i<4; i++){
            String [] v = new String[] {input[(i*4)], input[(i*4)+1], input[(i*4)+2], input[(i*4)+3]};
            String[] temp = gColoum(v);
            r[(i*4)] = temp[0];
            r[(i*4)+1] = temp[1];
            r[(i*4)+2] = temp[2];
            r[(i*4)+3] = temp[3];
            //System.out.println((i*4)+3);
        }
        return r;
    }
    String[] gColoum(String[] v){
        //http://en.wikipedia.org/wiki/Rijndael_mix_columns
        int[] q = new int[4];
        int[] w = new int[4];
        int h;
        int[] r = new int[4];
        for(int i=0; i<4; i++){
            int t = Integer.parseInt(v[i], 16);
            q[i] = t; // ri
            h = (t>>7)*0x1FF; //if leftmost bit is 1: 0x1FF, otherwise: 0
            //System.out.println(Integer.toBinaryString(h));
            w[i] = t<<1; 
            w[i]^= 0x11B & h; // 2*ri
        }
        r[0] = w[0] ^ w[1]^q[1] ^ q[2] ^ q[3]; //2v0 + 3v1 + 1v2 + 1v3
        r[1] = q[0] ^ w[1] ^ w[2]^q[2] ^ q[3]; //1v0 + 2v1 + 3v2 + 1v3
        r[2] = q[0] ^ q[1] ^ w[2] ^ w[3]^q[3]; //1v0 + 1v1 + 2v2 + 3v3
        r[3] = q[0]^w[0] ^ q[1] ^ q[2] ^ w[3]; //3v0 + 1v1 + 1v2 + 2v3

        return new String[] {
            String.format("%02X",r[0]), 
            String.format("%02X",r[1]), 
            String.format("%02X",r[2]), 
            String.format("%02X",r[3])
        };
    }
    String[] invMixColumns(String[] input){
        isValidVector(input);
        
        String[] r = new String[input.length];
        for(int i=0; i<4; i++){
            String [] v = new String[] {input[(i*4)], input[(i*4)+1], input[(i*4)+2], input[(i*4)+3]};
            String[] temp = invgColoum(v);
            r[(i*4)] = temp[0];
            r[(i*4)+1] = temp[1];
            r[(i*4)+2] = temp[2];
            r[(i*4)+3] = temp[3];
            //System.out.println((i*4)+3);
        }
        return r;
    }
    String[] invgColoum(String[] v){
        //http://en.wikipedia.org/wiki/Rijndael_mix_columns

        int[] r = new int[4];
        int t0 = Integer.parseInt(v[0], 16);
        int t1 = Integer.parseInt(v[1], 16);
        int t2 = Integer.parseInt(v[2], 16);        
        int t3 = Integer.parseInt(v[3], 16);

        r[0] = mul14[t0] ^ mul11[t1] ^ mul13[t2] ^ mul9[t3];
        r[1] = mul9[t0] ^ mul14[t1] ^ mul11[t2] ^ mul13[t3];
        r[2] = mul13[t0] ^ mul9[t1] ^ mul14[t2] ^ mul11[t3];
        r[3] = mul11[t0] ^ mul13[t1] ^ mul9[t2] ^ mul14[t3];

        return new String[] {
            String.format("%02X",r[0]), 
            String.format("%02X",r[1]), 
            String.format("%02X",r[2]), 
            String.format("%02X",r[3])
        };
    }
//
//        ########  ##    ## ######## ##    ## 
//   ##   ##     ## ##   ##  ##        ##  ##  
//   ##   ##     ## ##  ##   ##         ####   
// ###### ########  #####    ######      ##    
//   ##   ##   ##   ##  ##   ##          ##    
//   ##   ##    ##  ##   ##  ##          ##    
//        ##     ## ##    ## ########    ##    
    String[] AddRoundKey( String[] input ){
        isValidVector(input);
        String[] r = new String[input.length];
        String[] rkey = roundKey[round];
        for(int i=0; i<16; i++){
            r[i] = xor( input[i], rkey[i] );
        }
        return r; 
    }

//
// ##    ## ######## ##    ## ######## ##     ## ########  
// ##   ##  ##        ##  ##  ##        ##   ##  ##     ## 
// ##  ##   ##         ####   ##         ## ##   ##     ## 
// #####    ######      ##    ######      ###    ########  
// ##  ##   ##          ##    ##         ## ##   ##        
// ##   ##  ##          ##    ##        ##   ##  ##        
// ##    ## ########    ##    ######## ##     ## ##        

    void KeyExpansion( String[] cipherkey ){
        roundKey[0] = cipherkey;

        for( int i=4; i<44; i++){
            String[] word_i_1 = {
                roundKey[(i-1)/4][((i-1)%4)*4+0],
                roundKey[(i-1)/4][((i-1)%4)*4+1],
                roundKey[(i-1)/4][((i-1)%4)*4+2],
                roundKey[(i-1)/4][((i-1)%4)*4+3]};
                
            String[] word_i_4 = {
                roundKey[(i-4)/4][((i-4)%4)*4+0],
                roundKey[(i-4)/4][((i-4)%4)*4+1],
                roundKey[(i-4)/4][((i-4)%4)*4+2],
                roundKey[(i-4)/4][((i-4)%4)*4+3]};

            if( i%4 == 0){
                //rotate word_i-1
                String[] t = new String[] {word_i_1[1], word_i_1[2], word_i_1[3], word_i_1[0]};
                //subBytes
                t = new String[] {
                    S_BOX[Integer.parseInt(t[0].substring(0,1), 16)][Integer.parseInt(t[0].substring(1,2), 16)], 
                    S_BOX[Integer.parseInt(t[1].substring(0,1), 16)][Integer.parseInt(t[1].substring(1,2), 16)], 
                    S_BOX[Integer.parseInt(t[2].substring(0,1), 16)][Integer.parseInt(t[2].substring(1,2), 16)], 
                    S_BOX[Integer.parseInt(t[3].substring(0,1), 16)][Integer.parseInt(t[3].substring(1,2), 16)]
                };
                //xor word i-4
                t = new String[] {xor(t[0],word_i_4[0]), xor(t[1],word_i_4[1]), xor(t[2],word_i_4[2]), xor(t[3],word_i_4[3])};
                //xor Rcon
                t[0] = xor(t[0],Rcon[i/4]);
                roundKey[i/4][(i%4)*4+0] = t[0];
                roundKey[i/4][(i%4)*4+1] = t[1];
                roundKey[i/4][(i%4)*4+2] = t[2];
                roundKey[i/4][(i%4)*4+3] = t[3];
            }else{
                roundKey[i/4][(i%4)*4+0] = xor(word_i_1[0],word_i_4[0]);
                roundKey[i/4][(i%4)*4+1] = xor(word_i_1[1],word_i_4[1]);
                roundKey[i/4][(i%4)*4+2] = xor(word_i_1[2],word_i_4[2]);
                roundKey[i/4][(i%4)*4+3] = xor(word_i_1[3],word_i_4[3]);
            }

        }
    }
    static String xor(String a, String b){
        a = a.toUpperCase();
        b = b.toUpperCase();
        String r ="";
        int i = Integer.parseInt(a.substring(0,1), 16);
        int j = Integer.parseInt(b.substring(0,1), 16);
        int p = Integer.parseInt(a.substring(1,2), 16);
        int q = Integer.parseInt(b.substring(1,2), 16);
        
        r = String.format("%X%X", i^j, p^q);
        return r;
    }

    static boolean isHexStr(String input){
        input = input.toUpperCase();

        if( input.length()==2 ){
            boolean m0 = Pattern.matches( "[A-F0-9]", input.substring(0,1) );
            boolean m1 = Pattern.matches( "[A-F0-9]", input.substring(1,2) );
            return m0 && m1;
        }
        return false;
    }
    static boolean isValidVector(String[] input){
        if( input.length == 16 ){
            for(int i=0; i<16; i++){
                if( !isHexStr( input[i] ) ){
                    throw new IllegalInputException("Input contains non-hex charicaters:" +input[i]);
                }
            }
        } else {
            throw new IllegalInputException("Input should be 128 bits");
        }
        return true;
    }
    public static void printState(String[] state){
        isValidVector(state);
        System.out.printf("%s  %s  %s  %s\n", state[0], state[4], state[8], state[12]);
        System.out.printf("%s  %s  %s  %s\n", state[1], state[5], state[9], state[13]);
        System.out.printf("%s  %s  %s  %s\n", state[2], state[6], state[10], state[14]);
        System.out.printf("%s  %s  %s  %s\n", state[3], state[7], state[11], state[15]);
        System.out.println("----");
    }
    public static void printState(byte[] state){
        isValidVector( bytelist_to_stringlist(state) );
        System.out.printf("%02X  %02X  %02X  %02X\n", state[0], state[4], state[8], state[12]);
        System.out.printf("%02X  %02X  %02X  %02X\n", state[1], state[5], state[9], state[13]);
        System.out.printf("%02X  %02X  %02X  %02X\n", state[2], state[6], state[10], state[14]);
        System.out.printf("%02X  %02X  %02X  %02X\n", state[3], state[7], state[11], state[15]);
        System.out.println("----");

    }
    public static String[] toVector(String input){
        if( input.length() > 16 ){
            throw new IllegalInputException("Input too long, limit input length in 16 characters.");
        }
        String[] v = new String[16];
        for(int i=0; i<16; i++){

            if( i< input.length() ){
                v[i] = String.format("%02X",(int)input.charAt(i) );
            }else{
                v[i] = "00";
            }
        }
        return v;
    }
    public static String toPlainString(String[] input){
        isValidVector(input);
        String r = "";
        for(int i=0; i<16; i++){
            r+= String.format("%c",(char)Integer.parseInt(input[i], 16));
        }
        return r;
    }
    public static String[] bytelist_to_stringlist(byte[] array){
        String[] r = new String[array.length];
        for(int i=0; i<array.length; i++){
            r[i] = String.format("%02X", (int)(array[i] & 0xff));
        }
        return r;
    }
    public static byte[] stringlist_to_bytelist(String[] array){
        byte[] r = new byte[array.length];
        for(int i=0; i<array.length; i++){
            r[i] = (byte)Integer.parseInt(array[i], 16);
        }
        return r;
    }

    public byte[] encrypt(byte[] plain, byte[] cipherkey){
        String[] t = encrypt( bytelist_to_stringlist(plain), bytelist_to_stringlist(cipherkey) );
        return stringlist_to_bytelist(t);
    }
    public byte[] decrypt(byte[] cipher, byte[] cipherkey){
        String[] t = decrypt( bytelist_to_stringlist(cipher), bytelist_to_stringlist(cipherkey) );
        return stringlist_to_bytelist(t);
    };
}
