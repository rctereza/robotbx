package com.rctereza.robotbx.ztest;

import java.io.IOException;

import com.rctereza.robotbx.tools.FileUtils;

public class ResetDerbyDB {

	public static void main(String[] args) {

		String userHome = System.getProperty("user.home");

		String derbyVisibleFolder = userHome + "\\ReceitanetBX";
		String derbyHiddenFolder = userHome + "\\.receitanetbx";
		
		try {
			
			System.out.println("Deleting folder [" + derbyVisibleFolder + "]");
			FileUtils.removeDirectory(derbyVisibleFolder);

			System.out.println("Deleting folder [" + derbyHiddenFolder + "]");
			FileUtils.removeDirectory(derbyHiddenFolder);
			
		} catch (IOException e) {
			String message = e.getMessage();
			System.err.println(message);
			if (message.contains("it is being used")) {
				System.out.println("Feche o Receitanet BX. O sistema será reponsável por abrir ele.");
			}
		}
		
		System.out.println("Done!");
		
	}

}
