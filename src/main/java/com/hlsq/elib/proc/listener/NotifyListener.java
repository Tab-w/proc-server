package com.hlsq.elib.proc.listener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SessionAwareMessageListener;

import com.hlsq.elib.proc.service.ProcService;
import com.hlsq.elib.proc.service.ProducerService;
import com.hlsq.elib.proc.util.ThreadPool;

public class NotifyListener implements SessionAwareMessageListener<ObjectMessage> {
	@Autowired
	private Destination convertDestination;
	@Autowired
	private ThreadPool threadPool;
	@Autowired
	private ProcService procService;
	@Autowired
	private ProducerService producerService;
	private Logger log = Logger.getLogger(NotifyListener.class);

	public void onMessage(ObjectMessage message, Session session) throws JMSException {
		log.info("接收到通知消息:" + message.getObject());
		while (true) {
			// 判断该服务器是否有闲置线程
			if (ThreadPool.getWaitTasknumber() == 0 && ThreadPool.getWorkingTasknumber() < ThreadPool.getWorker_num()) {
				log.info("等待任务数:" + ThreadPool.getWaitTasknumber() + ",执行任务数:" + ThreadPool.getWorkingTasknumber());
				// 从convertDestination获取转换信息
				Map<String, Object> info = producerService.receiveMessage(convertDestination);
				if (info != null) {
					threadPool.execute(new Runnable() {
						public void run() {
							try {
								long start = System.currentTimeMillis();
								log.info("准备拆分Pdf:" + info.get("inputPath"));
								int status = procService.splitPdf(info.get("pdfOutputPath").toString(),
										info.get("inputPath").toString(),
										Integer.parseInt(info.get("pdfTimeout").toString()),
										info.get("splitPdfInfoFile").toString(), info.get("splitPdfErrFile").toString(),
										info.get("os").toString());
								if (status == 1) {
									log.info("准备转换Json:" + info.get("inputPath"));
									status = procService.convertToJson(info.get("jsonOutputPath").toString(),
											info.get("inputPath").toString(),
											Integer.parseInt(info.get("jsonTimeout").toString()),
											info.get("convertToJsonInfoFile").toString(),
											info.get("convertToJsonErrFile").toString(), info.get("os").toString());
									if (status == 1) {
										log.info("准备拆分Swf:" + info.get("inputPath"));
										status = procService.convertToSwf(info.get("swfOutputPath").toString(),
												info.get("inputPath").toString(),
												Integer.parseInt(info.get("swfTimeout").toString()),
												info.get("convertToSwfInfoFile").toString(),
												info.get("convertToSwfErrFile").toString(), info.get("os").toString());
										if (status == 1) {
											// 转换成功
											Map<String, Object> returnMap = new HashMap<String, Object>();
											long total = System.currentTimeMillis() - start;
											log.info("转换" + info.get("name") + "成功,用时:" + total / 1000 + "秒");
											returnMap.put("message", "转换:" + info.get("inputPath") + "完成");
											producerService.sendObjectMessage(
													(Destination) info.get("localDestination"), returnMap);
										} else {
											// 转换Swf失败
											Map<String, Object> returnMap = new HashMap<String, Object>();
											returnMap.put("message", "转换:" + info.get("inputPath") + "----Swf失败");
											producerService.sendObjectMessage(
													(Destination) info.get("localDestination"), returnMap);
										}
									} else {
										// 转换Json失败
										Map<String, Object> returnMap = new HashMap<String, Object>();
										returnMap.put("message", "转换:" + info.get("inputPath") + "----Json失败");
										producerService.sendObjectMessage((Destination) info.get("localDestination"),
												returnMap);
									}
								} else {
									// 拆分Pdf失败
									Map<String, Object> returnMap = new HashMap<String, Object>();
									returnMap.put("message", "转换:" + info.get("inputPath") + "----Pdf失败");
									producerService.sendObjectMessage((Destination) info.get("localDestination"),
											returnMap);
								}
							} catch (NumberFormatException e) {
								e.printStackTrace();
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
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
				} else {
					log.info("没有获取到转换信息");
					return;
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
