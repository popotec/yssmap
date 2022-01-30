package yssmap.stationapi.domain;

public class ApiFetchResult {
	private int countOfCreated;
	private int countOfChanged;
	private int countOfNotChanged;

	public ApiFetchResult() {
	}

	public void increaseCountOfCreated() {
		this.countOfCreated++;
	}

	public void increaseCountOfChanged() {
		this.countOfChanged++;
	}

	public void increaseCountOfNotChanged() {
		this.countOfNotChanged++;
	}

	@Override
	public String toString() {
		return "api fetch 결과 {" +
			"countOfCreated=" + countOfCreated +
			", countOfChanged=" + countOfChanged +
			", countOfNotChanged=" + countOfNotChanged +
			'}';
	}
}
