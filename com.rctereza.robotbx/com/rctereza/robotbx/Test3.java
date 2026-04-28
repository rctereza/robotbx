package com.rctereza.robotbx;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

public class Test3 {

	public static void main(String[] args) {
//		String subject = "CN=JALAPAO IMPORTADORA EXPORTADORA COMERCIO DE LUBRI:07214419000195, OU=presencial, OU=RFB e-CNPJ A1, OU=Secretaria da Receita Federal do Brasil - RFB, OU=38132981000101, L=PALMAS, ST=TO, O=ICP-Brasil, C=BR";
//		
//		String customer = subject.substring(3,subject.indexOf(","));
//		
//		String[] values = customer.split(":");
//		
//		System.out.println(values[0]);
//		System.out.println(values[1]);

//		while (true) {
//			// Get pointer info
//			PointerInfo pointerInfo = MouseInfo.getPointerInfo();
//			Point point = pointerInfo.getLocation();
//
//			int x = (int) point.getX();
//			int y = (int) point.getY();
//
//			System.out.println("Mouse position: X=" + x + " Y=" + y);
//
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} // pause for three seconds
//		}
		
//		String value = "07214419000195-293845298-20210401-20210430-0-9535ADC3A5F5892956F5ECDEDE1E6D397F8FE8B4-SPED-EFD.txt";
//		
//		String value2 = value.substring(value.indexOf("-")+1);
//		System.out.println(value2);
//		
//		value2 = value2.substring(value2.indexOf("-")+1);
//		System.out.println(value2);
//		
//		value2 = value2.substring(0, 17).replace("-","_");
//		System.out.println(value2);
		
		String[] value = "31/12/2025".split("/");
		
		System.out.println(value[2]);
		System.out.println(value[1]);
		
		
		
	}

}
