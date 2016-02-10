package land.sebastianwie.shopware_uploadtool.api;

public class ArticleUploadStatus {
	private String message;
	private int id;

	public ArticleUploadStatus(int id, String message) {
		this.id = id;
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
