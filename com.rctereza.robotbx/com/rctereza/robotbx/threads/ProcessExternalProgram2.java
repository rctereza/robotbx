package com.rctereza.robotbx.threads;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.util.concurrent.CountDownLatch;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.models.Certificate;
import com.rctereza.robotbx.tools.Actions;

public class ProcessExternalProgram2 implements Runnable {

	private static final String threadName = "[" + ProcessExternalProgram2.class.getName() + "] ";

	private final CountDownLatch finishLatch;
	private final Certificate certificate;

	private final Actions actions;

	public ProcessExternalProgram2(CountDownLatch finishLatch, Certificate certificate) throws AWTException {
		this.finishLatch = finishLatch;
		this.certificate = certificate;
		actions = new Actions();
	}

	@Override
	public void run() {

		// Espera o programa abrir
		actions.Wait();

		// Clica no botão "Buscar Certificado"
		actions.Move(451, 565);
		actions.Click();

		// Colar o caminho do certificado
		actions.Move(598, 542);
		actions.Click();
		actions.Paste(certificate.FILE_PATH() + "\\" + certificate.FILE_NAME());

		// Clica no botão "Escolha"
		actions.Move(923, 541);
		actions.Click();

		// Colar o password do certificado
		actions.Move(617, 386);
		actions.Click();
		actions.Paste(certificate.FILE_PASS());

		// Clica no botão "OK"
		actions.Move(640, 414);
		actions.Click();

//			//Clicar no campo "selecionar o perfil"
//			actions.Move(850, 533); 
//			actions.Click();
//
//			//selecionar o perfil Procurador
//			actions.Move(844, 566); 
//			actions.Click();

		// Clica no botão "Entrar"
		actions.Move(875, 568);
		actions.Click();

		// Clica na opção "Pesquisa"
		actions.Move(264, 118);
		actions.Click();

		// Clica no campo "Selecione Sistema"
		actions.Move(887, 221);
		actions.Click();

		// Clica na opção "SPED Contribuições"
		actions.Move(827, 243);
		actions.Click();

//			//Clica na opção "SPED Contabil"
//			actions.Move(710, 255); 
//			actions.Click();
//			
//			//Clica na opção "SPED ECF"
//			actions.Move(688, 267); 
//			actions.Click();
//			
//			//Clica na opção "SPED EFD-Reinf"
//			actions.Move(679, 286); 
//			actions.Click();
//			
//			//Clica na opção "SPED Fiscal-EFD ICMS IPI"
//			actions.Move(701, 297); 
//			actions.Click();

		// Clica no campo "Selecione um tipo de arquivo"
		actions.Move(729, 252);
		actions.Click();

		// Clica na opção "Escrituração"
		actions.Move(727, 261);
		actions.Click();

		// Clica no campo "Selecione um tipo de pesquisa"
		actions.Move(674, 279);
		actions.Click();

		// Clica na opção "Periodo de Entrega"
		actions.Move(597, 295);
		actions.Click();

		// Clica no campo "Data de Inicio"
		actions.Move(519, 322);
		actions.Click();
		//actions.Type(Constants.PROGRAM_PERIOD_START);
		actions.Paste(Constants.PROGRAM_PERIOD_START);
		//actions.Tab();

		// Clica no campo "Data de Fim"
		actions.Move(497, 341);
		actions.Click();
		//actions.Type(Constants.PROGRAM_PERIOD_END);
		actions.Paste(Constants.PROGRAM_PERIOD_END);
		actions.Tab();

		// Clica no botao "Pesquisar"
		actions.Move(659, 594);
		actions.Click();

//		// Clica no botao "OK" da mensagem que não existe arquivos para o periodo informado.
//		actions.Wait();
//		actions.Move(720, 404);
//		actions.Click();

		// Clica no botao "Sair"
		actions.Wait();
		actions.Move(485, 147);
		actions.Click();

		while (true) {

			if (finishLatch.getCount() == 0) {
				break;
			}

			// Get pointer info
			PointerInfo pointerInfo = MouseInfo.getPointerInfo();
			Point point = pointerInfo.getLocation();

			int x = (int) point.getX();
			int y = (int) point.getY();

			System.out.println(threadName + "Mouse position: X=" + x + " Y=" + y);

			// Sleep to avoid flooding output
			try {
				Thread.sleep(5000); // pause for five seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println(threadName + "Program terminated.");

	}

}
