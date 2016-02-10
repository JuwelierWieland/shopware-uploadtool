package land.sebastianwie.shopware_uploadtool.resources.article;

import java.util.HashMap;
import java.util.Map;

import land.sebastianwie.shopware_uploadtool.util.ToJsonConvertible;

import org.json.JSONObject;

public class ArticleVariant implements ToJsonConvertible {

	private int motherId = -1;

	public static Map<String, Integer> numberIdMap = new HashMap<>();

	private String motherNumber;

	private ArticleDetail detail = new ArticleDetail();

	public ArticleDetail getDetail() {
		return detail;
	}

	public void setDetail(ArticleDetail detail) {
		this.detail = detail;
	}

	public int getMotherId() {
		if (motherId >= 0)
			return motherId;
		else if (numberIdMap.containsKey(motherNumber))
			return numberIdMap.get(motherNumber);
		else
			return -1;
	}

	public void setMotherId(int motherId) {
		this.motherId = motherId;
	}

	public String getMotherNumber() {
		return motherNumber;
	}

	public void setMotherNumber(String motherNumber) {
		this.motherNumber = motherNumber;
	}

	@Override
	public JSONObject toJSONObject() {
		if (getMotherId() != -1) {
			JSONObject result = detail.toJSONObject();
			result.put("articleId", getMotherId());
			return result;
		}
		return null;
	}
	
	public static ArticleVariant createFromJSONObject(JSONObject data) {
		ArticleVariant result = new ArticleVariant();
		result.setDetail(ArticleDetail.createFromJSONObject(data));
		result.setMotherId(data.getInt("articleId"));
		return result;
	}

}
