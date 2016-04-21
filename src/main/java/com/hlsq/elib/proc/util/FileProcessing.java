package com.hlsq.elib.proc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class FileProcessing {
	private String OS = "linux";
	private Process process = null;
	private Runtime runtime = null;
	private String infoFile = null;// INFO文件路径
	private String errFile = null;// ERROR文件路径
	private int status = -100;// 操作状态,-1:失败、0:不允许操作、1:成功、100:超时
	private String f_separator = System.getProperty("file.separator");// 文件分割符
	private String l_separator = System.getProperty("line.separator");// 回行符
	private Logger log = Logger.getLogger(FileProcessing.class);

	public FileProcessing() {
	}

	/**
	 * 
	 * @param OS
	 *            操作系统默认windows
	 * @param infoFile
	 *            信息文件路径
	 * @param errFile
	 *            报错文件路径
	 */
	public FileProcessing(String OS, String infoFile, String errFile) {
		this.runtime = Runtime.getRuntime();
		this.OS = OS;
		try {
			if (infoFile != "" && errFile != "") {
				File fileInfo = new File(infoFile);
				if (!fileInfo.exists()) {
					fileInfo.createNewFile();
				}
				File fileErr = new File(errFile);
				if (!fileErr.exists()) {
					fileErr.createNewFile();
				}
				this.infoFile = infoFile;
				this.errFile = errFile;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拆分PDF
	 * 
	 * @param inputFile
	 *            输入文件
	 * @param outputPath
	 *            输出目录
	 * @param timeout
	 *            超时限制
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public int splitToPdf(String inputFile, String outputPath, long timeout) throws IOException, InterruptedException {

		String notExtName = FileUtil.getNotExtName(inputFile);
		String fileName = FileUtil.getFileName(inputFile);

		// 创建输出目录否则拆分PDF会报错
		createDir(outputPath + f_separator + notExtName);

		// linux中存放pid的临时文件
		String pidFilePath = getPidFilePath();

		// 拆分PDF命令
		List<String> commandList = new ArrayList<String>();
		if (OS.equals("windows")) {
			commandList.add("cmd");
			commandList.add("/c");
			commandList.add("pdftk");
			commandList.add(inputFile);
			commandList.add("burst");
			commandList.add("output");
			commandList.add(outputPath + f_separator + notExtName + f_separator + notExtName + "_%1d.pdf");
			commandList.add("compress");
		} else {
			commandList.add("start-stop-daemon");
			commandList.add("--start");
			commandList.add("--quiet");
			commandList.add("--pidfile");
			commandList.add(pidFilePath);
			commandList.add("--m");
			commandList.add("--exec");
			commandList.add("/bin/pdftk");
			commandList.add(inputFile);
			commandList.add("burst");
			commandList.add("output");
			commandList.add(outputPath + f_separator + notExtName + f_separator + notExtName + "_%1d.pdf");
			commandList.add("compress");
		}
		// 设置成默认值
		status = -100;
		process = runtime.exec(commandList.toArray(new String[] {}));
		wirteToLog(process, infoFile, errFile, fileName, timeout, pidFilePath);
		process.waitFor();
		int exitValue = process.exitValue();
		if (exitValue == 0) {
			if (status == -100) {
				status = 1;
			}
		} else {
			if (status == -100) {
				status = -1;
			}
		}
		FileUtil.deleteFile(pidFilePath);
		return status;
	}

	/**
	 * 转换成JSON
	 * 
	 * @param inputFile
	 *            输入文件
	 * @param outputPath
	 *            输出目录
	 * @param timeout
	 *            超时限制
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public int convertToJson(String inputFile, String outputPath, long timeout)
			throws IOException, InterruptedException {

		String notExtName = FileUtil.getNotExtName(inputFile);
		String fileName = FileUtil.getFileName(inputFile);

		// 创建输出目录否则转换JSON会报错
		createDir(outputPath + f_separator + notExtName);

		// linux中存放pid的临时文件
		String pidFilePath = getPidFilePath();

		// 转换JSON命令
		List<String> commandList = new ArrayList<String>();
		if (OS.equals("windows")) {
			commandList.add("cmd");
			commandList.add("/c");
			commandList.add("pdf2json");
			commandList.add(inputFile);
			commandList.add("-enc");
			commandList.add("UTF-8");
			commandList.add("-compress");
			commandList.add("-split");
			commandList.add("10");
			commandList.add(outputPath + f_separator + notExtName + f_separator + notExtName + "_%.js");
		} else {
			commandList.add("start-stop-daemon");
			commandList.add("--start");
			commandList.add("--quiet");
			commandList.add("--pidfile");
			commandList.add(pidFilePath);
			commandList.add("--m");
			commandList.add("--exec");
			commandList.add("/usr/pdf2json/bin/pdf2json");
			commandList.add(inputFile);
			commandList.add("--");
			commandList.add("-enc");
			commandList.add("UTF-8");
			commandList.add("-compress");
			commandList.add("-split");
			commandList.add("10");
			commandList.add(outputPath + f_separator + notExtName + f_separator + notExtName + "_%.js");
		}
		// 设置成默认值
		status = -100;
		process = runtime.exec(commandList.toArray(new String[] {}));
		wirteToLog(process, infoFile, errFile, fileName, timeout, pidFilePath);
		process.waitFor();
		if (process.exitValue() == 0) {
			if (status == -100) {
				status = 1;
			}
		} else {
			if (status == -100) {
				status = -1;
			}
		}
		FileUtil.deleteFile(pidFilePath);
		return status;
	}

	/**
	 * 转换成SWF
	 * 
	 * @param inputFile
	 *            输入文件
	 * @param outputPath
	 *            输出目录
	 * @param timeout
	 *            超时限制
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public int convertToSwf(String inputFile, String outputPath, long timeout)
			throws IOException, InterruptedException {

		String notExtName = FileUtil.getNotExtName(inputFile);
		String fileName = FileUtil.getFileName(inputFile);

		// 创建输出目录否则转换SWF会报错
		createDir(outputPath + f_separator + notExtName);

		// linux中存放pid的临时文件
		String pidFilePath = getPidFilePath();

		// 转换SWF命令
		List<String> commandList = new ArrayList<String>();
		if (OS.equals("windows")) {
			commandList.add("cmd");
			commandList.add("/c");
			commandList.add("pdf2swf");
			commandList.add(inputFile);
			commandList.add("-o");
			commandList.add(outputPath + f_separator + notExtName + f_separator + notExtName + "_%.swf");
			commandList.add("-f");
			commandList.add("-T");
			commandList.add("9");
			commandList.add("-t");
			commandList.add("-s");
			commandList.add("storeallcharacters");
			commandList.add("-s");
			commandList.add("linknameurl");
		} else {
			commandList.add("start-stop-daemon");
			commandList.add("--start");
			commandList.add("--quiet");
			commandList.add("--pidfile");
			commandList.add(pidFilePath);
			commandList.add("--m");
			commandList.add("--exec");
			commandList.add("/usr/swftools/bin/pdf2swf");
			commandList.add(inputFile);
			commandList.add("-o");
			commandList.add(outputPath + f_separator + notExtName + f_separator + notExtName
					+ "_%.swf -f -T 9 -t -s storeallcharacters -s linknameurl");
		}
		// 设置成默认值
		status = -100;
		process = runtime.exec(commandList.toArray(new String[] {}));
		wirteToLog(process, infoFile, errFile, fileName, timeout, pidFilePath);
		process.waitFor();
		if (process.exitValue() == 0) {
			if (status == -100) {
				status = 1;
			}
		} else {
			if (status == -100) {
				status = -1;
			}
		}
		FileUtil.deleteFile(pidFilePath);
		return status;
	}

	/**
	 * 创建目录
	 * 
	 * @param dirPath
	 * @throws IOException
	 */
	public void createDir(String dirPath) throws IOException {
		if (!new File(dirPath).exists()) {
			List<String> commandList = new ArrayList<String>();
			if (OS.equals("windows")) {
				commandList.add("cmd");
				commandList.add("/c");
				commandList.add("md");
				commandList.add(dirPath);
			} else {
				commandList.add("mkdir");
				commandList.add("-p");
				commandList.add(dirPath);
			}
			runtime.exec(commandList.toArray(new String[] {}));
		}
	}

	/**
	 * linux中存放pid的临时文件
	 * 
	 * @return
	 */
	public String getPidFilePath() {
		long head = System.currentTimeMillis();
		Random random = new Random();
		int lastNum = random.nextInt(10000);
		String pidFilePath = System.getProperty("user.dir") + "/" + head + lastNum + ".pid";
		return pidFilePath;
	}

	static interface Kernel32 extends Library {
		public static Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

		public int GetProcessId(Long hProcess);
	}

	/**
	 * 日志
	 * 
	 * @param process
	 * @param infoFile
	 * @param errFile
	 * @param fileName
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void wirteToLog(Process process, String infoFile, String errFile, String fileName, long timeout,
			String pidFilePath) {
		Thread threadErr = new Thread(new Runnable() {
			public void run() {
				try {
					final FileWriter fwErr = errFile != null ? new FileWriter(new File(errFile), true) : null;
					final InputStream isErr = process.getErrorStream();
					final InputStreamReader isrErr = new InputStreamReader(isErr, "GBK");
					final BufferedReader brErr = new BufferedReader(isrErr);
					String lineErr = null;
					while ((lineErr = brErr.readLine()) != null) {
						if (lineErr.indexOf("OWNER PASSWORD REQUIRED") > -1
								|| lineErr.indexOf("Copying of text from this document is not allowed") > 0) {
							// 加密的
							status = 0;
						}
						if (lineErr.indexOf("unable to open file for output") > -1) {
							// 不能打开doc_data.txt,不影响拆分
							status = 1;
						}
						if (lineErr.indexOf("Exception") > 0 || lineErr.indexOf("Unexpected") > 0
								|| lineErr.indexOf("pdf2json version") > -1) {
							// 内部异常
							status = -1;
						}
						lineErr = " [" + fileName + "----ERROR]  " + lineErr;
						log.info(lineErr);
						// 写入日志文件
						if (fwErr != null) {
							synchronized (fwErr) {
								fwErr.write(lineErr + l_separator);
								fwErr.flush();
							}
						}
					}
					fwErr.close();
					brErr.close();
					isrErr.close();
					isErr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		Thread threadInfo = new Thread(new Runnable() {
			public void run() {
				try {
					final FileWriter fwInfo = infoFile != null ? new FileWriter(infoFile, true) : null;
					final InputStream isInfo = process.getInputStream();
					final InputStreamReader isrInfo = new InputStreamReader(isInfo, "GBK");
					final BufferedReader brInfo = new BufferedReader(isrInfo);
					String lineInfo = null;
					while ((lineInfo = brInfo.readLine()) != null) {
						if (lineInfo.indexOf("FATAL   PDF disallows copying") > -1) {
							// 转换SWF失败,PDF加密
							status = 0;
						}
						lineInfo = " [" + fileName + "----INFO]  " + lineInfo;
						log.info(lineInfo);
						// 写入日志文件
						if (fwInfo != null) {
							synchronized (fwInfo) {
								fwInfo.write(lineInfo + l_separator);
								fwInfo.flush();
							}
						}
					}
					fwInfo.close();
					brInfo.close();
					isrInfo.close();
					isInfo.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		// 监听超时用的线程
		Thread threadDaemon = new Thread(new Runnable() {
			public void run() {
				long startTime = System.currentTimeMillis();
				long endTime;
				long totalTime;
				while (process.isAlive()) {
					endTime = System.currentTimeMillis();
					totalTime = endTime - startTime;
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (totalTime > timeout) {
						status = 100;
						if (OS.equals("windows")) {
							try {
								Field handle = process.getClass().getDeclaredField("handle");
								handle.setAccessible(true);
								final long pid = Kernel32.INSTANCE.GetProcessId((Long) handle.get(process));
								killThread(OS, Long.toString(pid));
							} catch (NoSuchFieldException e) {
								e.printStackTrace();
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							try {
								String pid = FileUtil.readFile(pidFilePath);
								System.out.println(pid);
								killThread(OS, pid);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						return;
					}
				}
			}
		});
		threadErr.start();
		threadInfo.start();
		threadDaemon.start();
	}

	/**
	 * 删除目录及文件
	 * 
	 * @param dirPath
	 * @throws IOException
	 */
	public void deleteDir(String dirPath) throws IOException {
		log.info("转换失败后删除相应的文件夹及文件:" + dirPath);
		List<String> comandList = new ArrayList<String>();
		if (OS.equals("windows")) {
			comandList.add("cmd");
			comandList.add("/c");
			comandList.add("rmdir");
			comandList.add("/s/q");
			comandList.add(dirPath);
		} else {
			comandList.add("rm");
			comandList.add("-rf");
			comandList.add(dirPath);
		}
		runtime.exec(comandList.toArray(new String[] {}));
	}

	/**
	 * 终止线程
	 * 
	 * @param name
	 * @throws IOException
	 */
	public void killThread(String os, String pid) throws IOException {
		if (os.equals("windows")) {
			Runtime.getRuntime().exec("Taskkill /f /T /IM " + pid);
		} else {
			Runtime.getRuntime().exec("kill -9 " + pid);
		}
	}

	public String getOS() {
		return OS;
	}

	public void setOS(String oS) {
		OS = oS;
	}

	public String getInfoFile() {
		return infoFile;
	}

	public void setInfoFile(String infoFile) {
		this.infoFile = infoFile;
	}

	public String getErrFile() {
		return errFile;
	}

	public void setErrFile(String errFile) {
		this.errFile = errFile;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
