package bros.manage.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 设备信息 2018.05.08 新增
 *
 */
public class DeviceInfo {
   public static String getDeviceIp() {
      try {
         return InetAddress.getLocalHost().getHostAddress();
      } catch (Exception e) {
         return "0.0.0.0";
      }
   }

   public static String getDeviceMAC() {
      try {
         return getMACAddress(InetAddress.getLocalHost());
      } catch (Exception e) {
         return "00-00-00-00-00-00";
      }
   }

   /**
    * 获取MAC地址的方法
    * 
    * @param ia
    * @return
    * @throws IOException
    */
   private static String getMACAddress(InetAddress ia) throws IOException {
      // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
      byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

      // 下面代码是把mac地址拼装成String
      StringBuilder sb = new StringBuilder();

      for (int i = 0; i < mac.length; i++) {
         if (i != 0) {
            sb.append("-");
         }
         // mac[i] & 0xFF 是为了把byte转化为正整数
         String s = Integer.toHexString(mac[i] & 0xFF);
         sb.append(s.length() == 1 ? 0 + s : s);
      }
      // 把字符串所有小写字母改为大写成为正规的mac地址并返回
      return sb.toString().toUpperCase();
   }
}
