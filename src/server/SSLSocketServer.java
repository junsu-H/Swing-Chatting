//package server;
//
//import java.io.*;
//import java.security.*;
//import javax.net.ssl.*;
//
//public class SSLSocketServer {
//
//	public static void main(String[] args) {
//		int PORT = 9825;
//
//		final KeyStore ks;
//		final KeyManagerFactory kmf;
//		final SSLContext sc;
//		String path = System.getProperty("user.dir");
//
//		SSLServerSocketFactory ssf = null;	// client ����
//		SSLServerSocket s = null;
//		SSLSocket c = null;
//
//		BufferedWriter w = null;
//		BufferedReader r = null;
//
//		String ksName = path + ".keystore/SSLSocketServerKey";
//
//		char keyStorePass[] = "234567".toCharArray();
//		char keyPass[] = "234567".toCharArray();
//
//		try {
//			ks = KeyStore.getInstance("JKS");
//			ks.load(new FileInputStream(ksName), keyStorePass);
//
//			kmf = KeyManagerFactory.getInstance("SunX509");
//			kmf.init(ks, keyPass);
//
//			sc = SSLContext.getInstance("TLS");
//			sc.init(kmf.getKeyManagers(), null, null);
//
//
//			/* SSLServerSocket */
//			ssf = sc.getServerSocketFactory();
//			s = (SSLServerSocket) ssf.createServerSocket(PORT);
//
//			c = (SSLSocket) s.accept();
//
//			w = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
//			r = new BufferedReader(new InputStreamReader(c.getInputStream()));
//
//			String m = "SSLSocket based reverse echo, Type some words.exit '.'";
//
//			w.write(m, 0, m.length());
//			w.newLine();
//			w.flush();
//
//			while ((m = r.readLine()) != null) {
//				if (m.equals("."))
//					break;
//				char[] a = m.toCharArray();
//				int n = a.length;
//				for (int i = 0; i < n / 2; i++) {
//					char t = a[i];
//					a[i] = a[n - 1 - i];
//					a[n - 1 - i] = t;
//				}
//				w.write(a, 0, n);
//				w.newLine();
//				w.flush();
//			}
//
//			w.close();
//			r.close();
//			s.close();
//			c.close();
//
//		} catch (SSLException se) {
//			System.out.println("SSL problem, exit~");
//			try {
//				w.close();
//				r.close();
//				s.close();
//				c.close();
//			} catch (IOException i) {
//			}
//		} catch (Exception e) {
//			System.out.println("What? exit~");
//			try {
//				w.close();
//				r.close();
//				s.close();
//				c.close();
//			} catch (IOException i) {
//			}
//		}
//	}
//
//}