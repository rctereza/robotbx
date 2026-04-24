package com.rctereza.robotbx.wrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

import com.rctereza.robotbx.models.ReceitaBx;

public class AppData implements Serializable {

	private static final long serialVersionUID = -16550397148575121L;

	private TreeMap<Integer, List<ReceitaBx>> receitaBxList = initialize();

	private TreeMap<Integer, List<ReceitaBx>> initialize() {
		return Objects.requireNonNullElse(receitaBxList, new TreeMap<>());
	}

	public void setReceitaBxList(TreeMap<Integer, List<ReceitaBx>> receitaBxList) {
		this.receitaBxList = receitaBxList;
	}

	public TreeMap<Integer, List<ReceitaBx>> getReceitaBxList() {
		return receitaBxList;
	}
	
	public void updateList(Integer id, List<ReceitaBx> list) {
		this.receitaBxList.put(id, list);
	}
	
	public void addList(List<ReceitaBx> list) {
		this.receitaBxList.put(getNextID(), list);
	}

	public Integer getLastIdAdded() {
		Integer result = 0;
		if (receitaBxList.size() > 0) {
			Entry<Integer, List<ReceitaBx>> lastEntry = receitaBxList.lastEntry();
			result = lastEntry.getKey();
		}
		return result;
	}

	public List<ReceitaBx> getLastListAdded() {
		List<ReceitaBx> result = new ArrayList<>();
		if (receitaBxList.size() > 0) {
			Entry<Integer, List<ReceitaBx>> lastEntry = receitaBxList.lastEntry();
			result = lastEntry.getValue();
		}
		return result;
	}

	private int getNextID() {
		int id = 0;
		if (receitaBxList.size() > 0) {
			Entry<Integer, List<ReceitaBx>> lastEntry = receitaBxList.lastEntry();
			id = lastEntry.getKey();
		}
		return (id + 1);
	}
}
 