package tracingmyjob;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MyTraceUtil {

	// 获取：当前程序运行在哪台主机
	public static String getHostname() {
		try {
			String hostname = InetAddress.getLocalHost().getHostName();
			return hostname;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 获取：当前程序运行时的进程唯一标识号(PID)
	public static int getProcsID() {
		try {
			String info = ManagementFactory.getRuntimeMXBean().getName();
			String pidString = info.substring(0, info.indexOf('@'));
			int pidInt = Integer.parseInt(pidString);
			return pidInt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 获取：当前程序运行时的线程名(thread name)
	public static String getThreadName() {
		try {
			String threadName = Thread.currentThread().getName();
			return threadName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 获取：当前类正在操作哪个具体的对象及其哈希码
	public static String getObjectInfo(Object obj) {
		try {
			String className = obj.getClass().getSimpleName();
			return className + "@" + obj.hashCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 获取完整的跟踪信息：主机名 + 进程名 + 线程名 + 处理的对象 + 自定义消息
	public static String getSummaryInfo(Object obj, String msg) {
		return getHostname() + " --- " + getProcsID() + " --- "
				+ getThreadName() + " --- " + getObjectInfo(obj) + " --- "
				+ msg;
	}
}
