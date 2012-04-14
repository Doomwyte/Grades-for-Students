package com.dyang.marks.courseObj;

public class CategoryObj {
	private int id;
	private String categoryName;
	private int course_id;
	private double weight;

	public CategoryObj(int id, String categoryName, int course_id, double weight) {
		setId(id);
		setCategoryName(categoryName);
		setCourse_id(course_id);
		setWeight(weight);
	}

	public CategoryObj() {

	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}

	public int getCourse_id() {
		return course_id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}
}
