package com.hlsq.elib.proc.service;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hlsq.elib.proc.util.FileProcessing;
import com.hlsq.elib.proc.util.FileUtil;

@Service
public class ProcService {
	private String f_separator = System.getProperty("file.separator");// 文件分割符
	private Logger log = Logger.getLogger(ProcService.class);

	/**
	 * 
	 * @param pdfOutputPath
	 *            输出路径
	 * @param inputPath
	 *            输入文件夹或文件
	 * @param timeout
	 *            超时限制
	 * @param infoFile
	 *            Info文件路径
	 * @param errFile
	 *            err文件路径
	 * @param OS
	 *            操作系统,example:"windows","linux"
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public int splitPdf(String pdfOutputPath, String inputPath, long timeout, String infoFile, String errFile,
			String OS) throws IOException, InterruptedException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		int status = -100;
		FileProcessing fileProcessing = new FileProcessing(OS, infoFile, errFile);
		File file = new File(inputPath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (FileUtil.getExt(f.getName()).equals(".pdf")) {
					long start = System.currentTimeMillis();
					String inputFile = f.getAbsolutePath();
					String notExtName = FileUtil.getNotExtName(inputFile);
					status = fileProcessing.splitToPdf(inputFile, pdfOutputPath, timeout);
					long total = System.currentTimeMillis() - start;
					log.info("拆分Pdf====" + notExtName + "; status:" + status + "; time:" + total);
					if (status != 1) {
						fileProcessing.deleteDir(pdfOutputPath + f_separator + notExtName);
					}
				}
			}
		} else {
			if (FileUtil.getExt(inputPath).equals(".pdf")) {
				long start = System.currentTimeMillis();
				String notExtName = FileUtil.getNotExtName(inputPath);
				status = fileProcessing.splitToPdf(inputPath, pdfOutputPath, timeout);
				long total = System.currentTimeMillis() - start;
				log.info("拆分Pdf====" + notExtName + "; status:" + status + "; time:" + total);
				if (status != 1) {
					fileProcessing.deleteDir(pdfOutputPath + f_separator + notExtName);
				}
			}
		}
		return status;
	}

	/**
	 * 
	 * @param pdfOutputPath
	 *            输出路径
	 * @param inputPath
	 *            输入文件夹或文件
	 * @param timeout
	 *            超时限制
	 * @param infoFile
	 *            Info文件路径
	 * @param errFile
	 *            err文件路径
	 * @param OS
	 *            操作系统,example:"windows","linux"
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public int convertToJson(String jsonOutputPath, String inputPath, long timeout, String infoFile, String errFile,
			String OS) throws IOException, InterruptedException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		int status = -100;
		FileProcessing fileProcessing = new FileProcessing(OS, infoFile, errFile);
		File file = new File(inputPath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (FileUtil.getExt(f.getName()).equals(".pdf")) {
					String inputFile = f.getAbsolutePath();
					long start = System.currentTimeMillis();
					String notExtName = FileUtil.getNotExtName(inputFile);
					status = fileProcessing.convertToJson(inputFile, jsonOutputPath, timeout);
					long total = System.currentTimeMillis() - start;
					log.info("转换Json====" + notExtName + "; status:" + status + "; time:" + total);
					if (status != 1) {
						fileProcessing.deleteDir(jsonOutputPath + f_separator + notExtName);
					}
				}
			}
		} else {
			if (FileUtil.getExt(inputPath).equals(".pdf")) {
				long start = System.currentTimeMillis();
				String notExtName = FileUtil.getNotExtName(inputPath);
				status = fileProcessing.convertToJson(inputPath, jsonOutputPath, timeout);
				long total = System.currentTimeMillis() - start;
				log.info("转换Json====" + notExtName + "; status:" + status + "; time:" + total);
				if (status != 1) {
					fileProcessing.deleteDir(jsonOutputPath + f_separator + notExtName);
				}
			}
		}
		return status;
	}

	/**
	 * 
	 * @param pdfOutputPath
	 *            输出路径
	 * @param inputPath
	 *            输入文件夹或文件
	 * @param timeout
	 *            超时限制
	 * @param infoFile
	 *            Info文件路径
	 * @param errFile
	 *            err文件路径
	 * @param OS
	 *            操作系统,example:"windows","linux"
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public int convertToSwf(String swfOutputPath, String inputPath, long timeout, String infoFile, String errFile,
			String OS) throws IOException, InterruptedException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		int status = -100;
		FileProcessing fileProcessing = new FileProcessing(OS, infoFile, errFile);
		File file = new File(inputPath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (FileUtil.getExt(f.getName()).equals(".pdf")) {
					String inputFile = f.getAbsolutePath();
					long start = System.currentTimeMillis();
					String notExtName = FileUtil.getNotExtName(inputFile);
					status = fileProcessing.convertToSwf(inputFile, swfOutputPath, timeout);
					long total = System.currentTimeMillis() - start;
					log.info("转换Swf====" + notExtName + "; status:" + status + "; time:" + total);
					if (status != 1) {
						fileProcessing.deleteDir(swfOutputPath + f_separator + notExtName);
					}
				}
			}
		} else {
			if (FileUtil.getExt(inputPath).equals(".pdf")) {
				long start = System.currentTimeMillis();
				String notExtName = FileUtil.getNotExtName(inputPath);
				status = fileProcessing.convertToSwf(inputPath, swfOutputPath, timeout);
				long total = System.currentTimeMillis() - start;
				log.info("转换Swf====" + notExtName + "; status:" + status + "; time:" + total);
				if (status != 1) {
					fileProcessing.deleteDir(swfOutputPath + f_separator + notExtName);
				}
			}
		}
		return status;
	}
}
