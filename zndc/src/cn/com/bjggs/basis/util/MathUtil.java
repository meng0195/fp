package cn.com.bjggs.basis.util;

public class MathUtil {
	
	/**
	 * 高地位无符号byte转int
	 * @author	wc
	 * @date	2017年7月14日
	 * @return	int
	 */
	public static final int byte2Int(byte h, byte l){
		return ((h & 0xFF) << 8) + (l & 0xFF);
	}
	
	/**
	 * 正负温度
	 * @param h
	 * @param l
	 * @return
	 */
	public static final int byte2IntNb(byte h, byte l){
		int temp = ((h & 0xFF) << 8) + (l & 0xFF);
		if(temp > 32768){
			temp = -((~temp & 0xFFFF) + 1);
		}
		return temp;
	}
	
	/**
	 * int 转高位byte
	 */
	public static final byte int2HByte(int i){
		return (byte)(i >> 8);
	}
	
	/**
	 * int 转低位byte
	 */
	public static final byte int2LByte(int i){
		return (byte)(i & 0xFF);
	}
	
	/**
	 * int 转高地位byte2
	 */
	public static final byte[] int2Bytes(int i){
		return new byte[]{int2HByte(i), int2LByte(i)};
	}
	
	/**
	 * 无符号byte 转int
	 */
	public static final int byte2int(byte b){
		return b & 0xFF;
	}
	
	/**
	 * crc
	 * @author	wc
	 * @date	2017年7月14日
	 * @return	int
	 */
	public static final byte[] crc40xC9DA(byte[] ds, int begin, int end){
        int flag; 
        // 16位寄存器，所有数位均为1  
        int r = 0xFFFF;  
        for (int i = begin; i < end; i++) {
        	
            r = r ^ (ds[i] & 0xFF);
  
            for (int j = 0; j < 8; j++) {
                flag = r & 0x0001;
                // 把这个 16 寄存器向右移一位  
                r = r >> 1;
                // 若向右(标记位)移出的数位是 1,则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算  
                if (flag == 1){
                    r ^= 0xC9DA;
                }
            }  
        }  
        return new byte[]{(byte)(r % 256), (byte)(r / 256)};
	}
	
	public static final byte changeTo1ByIndex(byte b, int index){
		return (byte)(b | (1 << index));
	}
	
	public static final byte changeTo0ByIndex(byte b, int index){
		return (byte)(b & (~(1 << index)));
	}
	
	public static final byte getBitByIndex(byte b, int index){
		return (byte)(((1 << index) & b) >> index);
	}
}
