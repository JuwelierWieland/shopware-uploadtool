package land.sebastianwie.shopware_uploadtool.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class DocumentUtils {
	
	public static Document fromURL(String url) {
		SAXReader reader = new SAXReader();
		Document result = null;
		try {
			result = reader.read(url);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}

}
