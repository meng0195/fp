package temp;

import java.text.DecimalFormat;

import org.junit.Test;

import cn.com.bjggs.datas.util.DB4MySQL;

public class TestUint16 {

	private int test16(byte[] ds, int len){
        int flag;  
  
        // 16位寄存器，所有数位均为1  
        int wcrc = 0xffff;  
        for (int i = 0; i < len; i++) {  
            // 16 位寄存器的高位字节  
         //   high = wcrc >> 8;  
            // 取被校验串的一个字节与 16 位寄存器的高位字节进行“异或”运算  
            wcrc = wcrc ^ (ds[i] & 0xFF);  
  
            for (int j = 0; j < 8; j++) {  
                flag = wcrc & 0x0001;  
                // 把这个 16 寄存器向右移一位  
                wcrc = wcrc >> 1;  
                // 若向右(标记位)移出的数位是 1,则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算  
                if (flag == 1){
                    wcrc ^= 0xC9DA;
                }
            }  
        }  
        wcrc = ((wcrc % 256) << 8) + (wcrc / 256);
        return wcrc;
	}
	
	//@Test
	public void test(){
		int num = test16(new byte[]{0x05, 0x64, 0x00, (byte)0xC9, 0x48, 0x00, 0x01}, 7);
		System.out.print(num);
	}
	
	//@Test
	public void test16(){
		System.out.print((byte)(600 >> 8));
		System.out.print((byte)(600 & 0xff));
	}
	
	//@Test
	public void testDb(){
		DB4MySQL.backup();
	}
	
	private void getWs(double a, double b, double c, double d){
		double d1 = (b - a)/10.0D;
		double[] ds1 = new double[11];
		for(int i = 0; i < 11; i++){
			ds1[i] = a + d1 * i;
		}
		double d2 = (d - c)/10.0D;
		double[] ds2 = new double[11];
		for(int i = 0; i < 11; i++){
			ds2[i] = c + d2 * i;
		}
		double[][] dss = new double[11][6];
		double d3 = 0;
		for(int i = 0; i < 11; i++){
			d3 = (ds1[i] - ds2[i])/5.0D;
			for(int j = 0; j < 6; j++){
				dss[i][j] = ds1[i] - d3 * j;
			}
		}
		DecimalFormat df = new DecimalFormat("#0.00");
		StringBuffer sb;
		for(int i = 0; i < 11; i++){
			sb = new StringBuffer();
			for(int j = 0; j < 6; j++){
				sb.append(df.format(dss[i][j]));
				sb.append(" ");
			}
			System.out.println(sb.toString());
		}
	}
	//小麦
//	double[] cc1 = new double[]{10.2, 11.6, 13.0, 14.3, 15.7, 17.2, 19.1, 22.3};
//	double[] cc2 = new double[]{9.8, 11.1, 12.5, 13.9, 15.2, 16.7, 18.7, 21.9};
//	double[] cc1 = new double[]{9.8, 11.1, 12.5, 13.9, 15.2, 16.7, 18.7, 21.9};
//	double[] cc2 = new double[]{9.4, 10.7, 12.1, 13.4, 14.8, 16.3, 18.3, 21.4};
//	double[] cc1 = new double[]{9.4, 10.7, 12.1, 13.4, 14.8, 16.3, 18.3, 21.4};
//	double[] cc2 = new double[]{8.9, 10.3, 11.6, 13.0, 14.4, 15.9, 17.8, 21.0};
//	double[] cc1 = new double[]{8.9, 10.3, 11.6, 13.0, 14.4, 15.9, 17.8, 21.0};
//	double[] cc2 = new double[]{8.5, 9.8, 11.2, 12.6, 13.9, 15.5, 17.4, 20.5};
//	double[] cc1 = new double[]{8.5, 9.8, 11.2, 12.6, 13.9, 15.5, 17.4, 20.5};
//	double[] cc2 = new double[]{8.0, 9.4, 10.8, 12.1, 13.5, 15.0, 16.9, 20.1};
//	double[] cc1 = new double[]{8.0, 9.4, 10.8, 12.1, 13.5, 15.0, 16.9, 20.1};
//	double[] cc2 = new double[]{7.6, 8.9, 10.3, 11.7, 13.1, 14.6, 16.5, 19.7};
//	double[] cc1 = new double[]{7.6, 8.9, 10.3, 11.7, 13.1, 14.6, 16.5, 19.7};
//	double[] cc2 = new double[]{7.2, 8.5, 9.9, 11.3, 12.6, 14.1, 16.1, 19.2};
	
//	double[] cc1 = new double[]{7.7, 9.0, 10.4, 11.9, 13.4, 15.1, 17.1, 21.3};
//	double[] cc2 = new double[]{7.4, 8.7, 10.1, 11.6, 13.1, 14.8, 16.8, 21};
//	double[] cc1 = new double[]{7.4, 8.7, 10.1, 11.6, 13.1, 14.8, 16.8, 21};
//	double[] cc2 = new double[]{7.1, 8.4, 9.8, 11.3, 12.8, 14.5, 16.5, 20.7};
//	double[] cc1 = new double[]{7.1, 8.4, 9.8, 11.3, 12.8, 14.5, 16.5, 20.7};
//	double[] cc2 = new double[]{6.8, 8.1, 9.5, 11.0, 12.5, 14.2, 16.2, 20.4};
//	double[] cc1 = new double[]{6.8, 8.1, 9.5, 11.0, 12.5, 14.2, 16.2, 20.4};
//	double[] cc2 = new double[]{6.5, 7.8, 9.2, 10.7, 12.2, 13.9, 15.8, 20.1};
//	double[] cc1 = new double[]{6.5, 7.8, 9.2, 10.7, 12.2, 13.9, 15.8, 20.1};
//	double[] cc2 = new double[]{6.2, 7.5, 8.9, 10.3, 11.9, 13.6, 15.5, 19.8};
//	double[] cc1 = new double[]{6.2, 7.5, 8.9, 10.3, 11.9, 13.6, 15.5, 19.8};
//	double[] cc2 = new double[]{5.9, 7.2, 8.6, 10.1, 11.6, 13.3, 15.2, 19.5};
//	double[] cc1 = new double[]{5.9, 7.2, 8.6, 10.1, 11.6, 13.3, 15.2, 19.5};
//	double[] cc2 = new double[]{5.6, 6.9, 8.3, 9.7, 11.3, 13.0, 14.9, 19.2};
//	稻谷
//	double[] cc1 = new double[]{9.9, 11.5, 12.9, 14.2, 15.6, 17.1, 18.6, 21.1};
//	double[] cc2 = new double[]{9.5, 11.1, 12.5, 13.8, 15.2, 16.6, 18.1, 20.7};
//	double[] cc1 = new double[]{9.5, 11.1, 12.5, 13.8, 15.2, 16.6, 18.1, 20.7};
//	double[] cc2 = new double[]{9.1, 10.6, 12.1, 13.4, 14.7, 16.2, 17.8, 20.2};
//	double[] cc1 = new double[]{9.1, 10.6, 12.1, 13.4, 14.7, 16.2, 17.8, 20.2};
//	double[] cc2 = new double[]{8.6, 10.2, 11.6, 12.9, 14.3, 15.7, 17.3, 19.8};
//	double[] cc1 = new double[]{8.6, 10.2, 11.6, 12.9, 14.3, 15.7, 17.3, 19.8};
//	double[] cc2 = new double[]{8.2, 9.8, 11.2, 12.5, 13.9, 15.4, 16.9, 19.4};
//	double[] cc1 = new double[]{8.2, 9.8, 11.2, 12.5, 13.9, 15.4, 16.9, 19.4};
//	double[] cc2 = new double[]{7.8, 9.4, 10.8, 12.1, 13.5, 14.9, 16.5, 19.0};
//	double[] cc1 = new double[]{7.8, 9.4, 10.8, 12.1, 13.5, 14.9, 16.5, 19.0};
//	double[] cc2 = new double[]{7.4, 9.0, 10.4, 11.7, 13.0, 14.5, 16.0, 18.5};
//	double[] cc1 = new double[]{7.4, 9.0, 10.4, 11.7, 13.0, 14.5, 16.0, 18.5};
//	double[] cc2 = new double[]{7.0, 8.6, 9.9, 11.2, 12.6, 14.1, 15.6, 18.1};
	
//	double[] cc1 = new double[]{7.8, 9.1, 10.4, 11.7, 13.1, 14.6, 16.4, 19.3};
//	double[] cc2 = new double[]{7.6, 8.9, 10.1, 11.4, 12.9, 14.3, 16.1, 19.0};
//	double[] cc1 = new double[]{7.6, 8.9, 10.1, 11.4, 12.9, 14.3, 16.1, 19.0};
//	double[] cc2 = new double[]{7.4, 8.7, 9.9, 11.2, 12.6, 14.1, 15.9, 18.8};
//	double[] cc1 = new double[]{7.4, 8.7, 9.9, 11.2, 12.6, 14.1, 15.9, 18.8};
//	double[] cc2 = new double[]{7.1, 8.4, 9.6, 11.0, 12.4, 13.8, 15.6, 18.5};
//	double[] cc1 = new double[]{7.1, 8.4, 9.6, 11.0, 12.4, 13.8, 15.6, 18.5};
//	double[] cc2 = new double[]{6.9, 8.2, 9.4, 10.7, 12.2, 13.6, 15.4, 18.3};
//	double[] cc1 = new double[]{6.9, 8.2, 9.4, 10.7, 12.2, 13.6, 15.4, 18.3};
//	double[] cc2 = new double[]{6.7, 8.0, 9.2, 10.5, 11.9, 13.4, 15.2, 18.1};
//	double[] cc1 = new double[]{6.7, 8.0, 9.2, 10.5, 11.9, 13.4, 15.2, 18.1};
//	double[] cc2 = new double[]{6.5, 7.8, 9.0, 10.3, 11.7, 13.1, 14.9, 17.8};
//	double[] cc1 = new double[]{6.5, 7.8, 9.0, 10.3, 11.7, 13.1, 14.9, 17.8};
//	double[] cc2 = new double[]{6.2, 7.5, 8.8, 10.0, 11.5, 12.9, 14.7, 17.6};	
	
//	大米
//	double[] cc1 = new double[]{10.4, 11.8, 13.1, 14.5, 15.8, 17.1, 18.8, 21.1};
//	double[] cc2 = new double[]{9.9, 11.5, 12.8, 14.1, 15.4, 16.7, 18.4, 20.7};	
	
//	double[] cc1 = new double[]{9.9, 11.5, 12.8, 14.1, 15.4, 16.7, 18.4, 20.7};
//	double[] cc2 = new double[]{9.6, 11.1, 12.4, 13.7, 15.0, 16.3, 18.0, 20.4};	
//	double[] cc1 = new double[]{9.6, 11.1, 12.4, 13.7, 15.0, 16.3, 18.0, 20.4};
//	double[] cc2 = new double[]{9.2, 10.7, 12.0, 13.3, 14.6, 15.9, 17.6, 20.0};	
//	double[] cc1 = new double[]{9.2, 10.7, 12.0, 13.3, 14.6, 15.9, 17.6, 20.0};
//	double[] cc2 = new double[]{8.8, 10.3, 11.6, 12.9, 14.3, 15.5, 17.3, 19.5};
//	double[] cc1 = new double[]{8.8, 10.3, 11.6, 12.9, 14.3, 15.5, 17.3, 19.5};
//	double[] cc2 = new double[]{8.4, 9.9, 11.2, 12.6, 13.9, 15.2, 16.9, 19.2};
//	double[] cc1 = new double[]{8.4, 9.9, 11.2, 12.6, 13.9, 15.2, 16.9, 19.2};
//	double[] cc2 = new double[]{8.0, 9.6, 10.8, 12.2, 13.5, 14.8, 16.5, 18.8};	
//	double[] cc1 = new double[]{8.0, 9.6, 10.8, 12.2, 13.5, 14.8, 16.5, 18.8};
//	double[] cc2 = new double[]{7.6, 9.1, 10.5, 11.8, 13.2, 14.4, 16.1, 18.5};
	
//	double[] cc1 = new double[]{8.7, 10.1, 11.4, 12.6, 14.0, 15.4, 17.1, 19.8};
//	double[] cc2 = new double[]{8.5, 9.9, 11.1, 12.4, 13.7, 15.1, 16.8, 19.6};	
//	double[] cc1 = new double[]{8.5, 9.9, 11.1, 12.4, 13.7, 15.1, 16.8, 19.6};
//	double[] cc2 = new double[]{8.3, 9.6, 10.9, 12.1, 13.4, 14.8, 16.6, 19.3};	
//	double[] cc1 = new double[]{8.3, 9.6, 10.9, 12.1, 13.4, 14.8, 16.6, 19.3};
//	double[] cc2 = new double[]{8.0, 9.4, 10.6, 11.9, 13.2, 14.6, 16.3, 19.0};
//	double[] cc1 = new double[]{8.0, 9.4, 10.6, 11.9, 13.2, 14.6, 16.3, 19.0};
//	double[] cc2 = new double[]{7.7, 9.1, 10.4, 11.6, 12.9, 14.3, 16.0, 18.7};
//	double[] cc1 = new double[]{7.7, 9.1, 10.4, 11.6, 12.9, 14.3, 16.0, 18.7};
//	double[] cc2 = new double[]{7.5, 8.8, 10.1, 11.4, 12.7, 14.1, 15.8, 18.5};	
//	double[] cc1 = new double[]{7.5, 8.8, 10.1, 11.4, 12.7, 14.1, 15.8, 18.5};
//	double[] cc2 = new double[]{7.2, 8.6, 9.8, 11.1, 12.4, 13.8, 15.5, 18.2};
//	double[] cc1 = new double[]{7.2, 8.6, 9.8, 11.1, 12.4, 13.8, 15.5, 18.2};
//	double[] cc2 = new double[]{7.0, 8.3, 9.6, 10.8, 12.2, 13.5, 15.2, 18.0};
	
//	玉米
//	double[] cc1 = new double[]{10.2, 11.5, 12.9, 14.2, 15.6, 17.0, 18.8, 21.7};
//	double[] cc2 = new double[]{9.8, 11.2, 12.5, 13.9, 15.2, 16.6, 18.4, 21.3};	
//	double[] cc1 = new double[]{9.8, 11.2, 12.5, 13.9, 15.2, 16.6, 18.4, 21.3};
//	double[] cc2 = new double[]{9.4, 10.8, 12.1, 13.5, 14.8, 16.2, 18.1, 20.9};	
//	double[] cc1 = new double[]{9.4, 10.8, 12.1, 13.5, 14.8, 16.2, 18.1, 20.9};
//	double[] cc2 = new double[]{8.9, 10.3, 11.7, 13.0, 14.3, 15.8, 17.7, 20.5};	
//	double[] cc1 = new double[]{8.9, 10.3, 11.7, 13.0, 14.3, 15.8, 17.7, 20.5};
//	double[] cc2 = new double[]{8.6, 10.0, 11.4, 12.7, 14.0, 15.5, 17.3, 20.2};
//	double[] cc1 = new double[]{8.6, 10.0, 11.4, 12.7, 14.0, 15.5, 17.3, 20.2};
//	double[] cc2 = new double[]{8.3, 9.7, 11.0, 12.3, 13.7, 15.1, 16.9, 19.8};
//	double[] cc1 = new double[]{8.3, 9.7, 11.0, 12.3, 13.7, 15.1, 16.9, 19.8};
//	double[] cc2 = new double[]{7.9, 9.3, 10.6, 12, 13.3, 14.7, 16.6, 19.4};	
//	double[] cc1 = new double[]{7.9, 9.3, 10.6, 12, 13.3, 14.7, 16.6, 19.4};
//	double[] cc2 = new double[]{7.5, 8.9, 10.2, 11.6, 12.9, 14.3, 16.2, 19.0};
	
//	double[] cc1 = new double[]{8.2, 9.3, 10.5, 11.9, 13.4, 15.0, 17.4, 20.9};
//	double[] cc2 = new double[]{7.9, 9.1, 10.3, 11.6, 13.1, 14.7, 17.1, 20.6};	
//	double[] cc1 = new double[]{7.9, 9.1, 10.3, 11.6, 13.1, 14.7, 17.1, 20.6};
//	double[] cc2 = new double[]{7.7, 8.8, 10.1, 11.4, 12.9, 14.4, 16.9, 20.4};	
//	double[] cc1 = new double[]{7.7, 8.8, 10.1, 11.4, 12.9, 14.4, 16.9, 20.4};
//	double[] cc2 = new double[]{7.4, 8.6, 9.8, 11.1, 12.6, 14.2, 16.7, 20.1};
//	double[] cc1 = new double[]{7.4, 8.6, 9.8, 11.1, 12.6, 14.2, 16.7, 20.1};
//	double[] cc2 = new double[]{7.2, 8.3, 9.5, 10.9, 12.4, 14.0, 16.3, 19.8};
//	double[] cc1 = new double[]{7.2, 8.3, 9.5, 10.9, 12.4, 14.0, 16.3, 19.8};
//	double[] cc2 = new double[]{6.9, 8.1, 9.3, 10.6, 12.1, 13.7, 16.1, 19.6};	
//	double[] cc1 = new double[]{6.9, 8.1, 9.3, 10.6, 12.1, 13.7, 16.1, 19.6};
//	double[] cc2 = new double[]{6.7, 7.8, 9.0, 10.3, 11.9, 13.4, 15.8, 19.3};
//	double[] cc1 = new double[]{6.7, 7.8, 9.0, 10.3, 11.9, 13.4, 15.8, 19.3};
//	double[] cc2 = new double[]{6.5, 7.6, 8.8, 10.1, 11.6, 13.2, 15.6, 19.1};
	@Test
	public void testcc(){
		//
//		double[] cc1 = new double[]{7.3, 8.2, 9.2, 10.4, 12.2, 14.4, 18.0, 24.9};
//		double[] cc2 = new double[]{6.9, 7.9, 8.9, 10.1, 11.8, 14.0, 17.1, 24.5};	
		
//		double[] cc1 = new double[]{6.9, 7.9, 8.9, 10.1, 11.8, 14.0, 17.1, 24.5};
//		double[] cc2 = new double[]{6.6, 7.6, 8.5, 9.7, 11.5, 13.7, 17.4, 24.2};
//		double[] cc1 = new double[]{6.6, 7.6, 8.5, 9.7, 11.5, 13.7, 17.4, 24.2};
//		double[] cc2 = new double[]{6.2, 7.2, 8.2, 9.4, 11.2, 13.3, 17.1, 23.9};
//		double[] cc1 = new double[]{6.2, 7.2, 8.2, 9.4, 11.2, 13.3, 17.1, 23.9};
//		double[] cc2 = new double[]{6.0, 6.9, 8.0, 9.1, 10.9, 13.0, 16.7, 23.5};
//		double[] cc1 = new double[]{6.0, 6.9, 8.0, 9.1, 10.9, 13.0, 16.7, 23.5};
//		double[] cc2 = new double[]{5.6, 6.5, 7.5, 8.7, 10.5, 12.7, 16.4, 23.2};
//		double[] cc1 = new double[]{5.6, 6.5, 7.5, 8.7, 10.5, 12.7, 16.4, 23.2};
//		double[] cc2 = new double[]{5.3, 6.2, 7.2, 8.4, 10.1, 12.4, 16.1, 22.9};
//		double[] cc1 = new double[]{5.3, 6.2, 7.2, 8.4, 10.1, 12.4, 16.1, 22.9};
//		double[] cc2 = new double[]{5.0, 5.9, 6.8, 8.1, 9.8, 12.0, 15.8, 22.6};
		
//		double[] cc1 = new double[]{5.6, 6.6, 7.8, 9.4, 11.2, 13.5, 17.4, 24.4};
//		double[] cc2 = new double[]{5.4, 6.4, 7.6, 9.2, 11.0, 13.3, 17.2, 24.2};	
//		double[] cc1 = new double[]{5.4, 6.4, 7.6, 9.2, 11.0, 13.3, 17.2, 24.2};
//		double[] cc2 = new double[]{5.2, 6.2, 7.4, 8.9, 10.8, 13.1, 16.9, 24.0};
//		double[] cc1 = new double[]{5.2, 6.2, 7.4, 8.9, 10.8, 13.1, 16.9, 24.0};
//		double[] cc2 = new double[]{5.0, 6.0, 7.2, 8.7, 10.6, 12.9, 16.7, 23.8};
//		double[] cc1 = new double[]{5.0, 6.0, 7.2, 8.7, 10.6, 12.9, 16.7, 23.8};
//		double[] cc2 = new double[]{4.7, 5.7, 6.9, 8.5, 10.3, 12.6, 16.5, 23.6};
//		double[] cc1 = new double[]{4.7, 5.7, 6.9, 8.5, 10.3, 12.6, 16.5, 23.6};
//		double[] cc2 = new double[]{4.6, 5.5, 6.7, 8.3, 10.1, 12.4, 16.3, 23.4};
//		double[] cc1 = new double[]{4.6, 5.5, 6.7, 8.3, 10.1, 12.4, 16.3, 23.4};
//		double[] cc2 = new double[]{4.4, 5.4, 6.5, 8.1, 9.9, 12.2, 16.1, 23.0};
		double[] cc1 = new double[]{4.4, 5.4, 6.5, 8.1, 9.9, 12.2, 16.1, 23.0};
		double[] cc2 = new double[]{4.0, 5.1, 6.3, 7.9, 9.7, 12.0, 15.8, 22.6};
		
		for(int i = 0; i < 7; i++){
			getWs(cc1[i], cc1[i+1], cc2[i], cc2[i+1]);
			System.out.println("");
		}
	}

}
