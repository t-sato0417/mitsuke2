import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


public class Main {
	public static void main(String args[]){
		XmlReader reader = new XmlReader();
		try {
			reader.domRead("sample.xml");
		} catch (SAXException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
