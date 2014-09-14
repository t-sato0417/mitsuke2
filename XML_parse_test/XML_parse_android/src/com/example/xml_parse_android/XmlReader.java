package com.example.xml_parse_android;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlReader {
	public void domRead(String file) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);

		Element root = document.getDocumentElement();
		
		//���[�g�v�f�̃m�[�h�����擾����
		System.out.println("�m�[�h���F" +root.getNodeName());

		//���[�g�v�f�̑������擾����
		System.out.println("���[�g�v�f�̑����F" + root.getAttribute("name"));

		//���[�g�v�f�̎q�m�[�h���擾����
		NodeList rootChildren = root.getChildNodes();

		System.out.println("�q�v�f�̐��F" + rootChildren.getLength());
		System.out.println("------------------");

		for(int i=0; i < rootChildren.getLength(); i++) {
			Node node = rootChildren.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)node;
				if (element.getNodeName().equals("person")) {
					System.out.println("���O�F" + element.getAttribute("name"));
					NodeList personChildren = node.getChildNodes();

					for (int j=0; j < personChildren.getLength(); j++) {
						Node personNode = personChildren.item(j);
						if (personNode.getNodeType() == Node.ELEMENT_NODE) {

							if (personNode.getNodeName().equals("age")) {
								System.out.println("�N��F" + personNode.getTextContent());
							} else if (personNode.getNodeName().equals("interest")) {
								System.out.println("�:" + personNode.getTextContent());
							}

						}
					}
					System.out.println("------------------");
				}
			}


		}


	}
}