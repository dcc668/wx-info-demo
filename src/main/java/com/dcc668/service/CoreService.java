package com.dcc668.service;

import com.dcc668.message.resp.TextMessage;
import com.dcc668.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * ���ķ�����
 *
 * @author
 * @date 2013-10-17
 */
public class CoreService {

	private static Logger LOGGER = LoggerFactory.getLogger(CoreService.class);

	/**
	 * ����΢�ŷ���������
	 *
	 * @param request
	 * @return xml
	 */
	public static String processRequest(HttpServletRequest request) {
		// xml��ʽ����Ϣ����
		String respXml = null;
		try {
			// ����parseXml��������������Ϣ
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// ���ͷ��ʺ�
			String fromUserName = requestMap.get("FromUserName");
			// ������΢�ź�
			String toUserName = requestMap.get("ToUserName");
			// ��Ϣ����
			String msgType = requestMap.get("MsgType");

			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

			// �¼�����
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				respXml = EventService.processEventRequest(requestMap);
			}
			// �ı���Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				textMessage.setContent("�����͵����ı���Ϣ��");
				respXml = MessageUtil.messageToXml(textMessage);
				LOGGER.info("-------------" + respXml);

				respXml = TextService.processTextRequest(requestMap);
				LOGGER.info("++++++++++++++" + respXml);
			}
			// ͼƬ��Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respXml = ImageService.processImageRequest(requestMap);
			}
			// ������Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				textMessage.setContent("�����͵���������Ϣ�����ڵ��ðٶȽӿڽ�����������");
				respXml = MessageUtil.messageToXml(textMessage);
			}
			// ��Ƶ��Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {
				textMessage.setContent("�����͵�����Ƶ��Ϣ��");
				respXml = MessageUtil.messageToXml(textMessage);
			}
			// ����λ����Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				textMessage.setContent("�����͵��ǵ���λ����Ϣ��");
				respXml = MessageUtil.messageToXml(textMessage);
			}
			// ������Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				textMessage.setContent("�����͵���������Ϣ��");
				respXml = MessageUtil.messageToXml(textMessage);
			}
			// ���û�����Ϣʱ
			else {
				textMessage.setContent("= = ����ͨ���˵�ʹ�õ�������");
				respXml = MessageUtil.messageToXml(textMessage);
			}
		} catch (Exception e) {
			LOGGER.info("CoreService============error:" + e.getMessage(), e);
			e.printStackTrace();
		}
		return respXml;
	}
}
