package com.iot.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.iot.DO.Checklist;
import com.iot.DO.ImageMaster;
import com.iot.DO.ItemMaster;
import com.iot.vo.ChecklistVO;

public interface ChecklistService {
	public List<ChecklistVO> getChecklist(String user_id)throws SQLException;
	
	public String addChecklist(Checklist checklist) throws SQLException, JSONException;

	public Map<String, Object> getSearchedItems(String searchedString) throws SQLException;
	
	public String updateChecklist(Checklist checklist) throws SQLException;
	
	public Map<String, Object> deleteChecklistItem(Checklist checklist);
	
	public Map<String, Object> getItemRecommendationForIndividualItem(ItemMaster itemMaster);

	public Map<String, Object> getItemFromItemMaster(String userName);

	public Map<String, Object> getImage(ImageMaster imageMaster);

	public Map<String, Object> getImageFromImageId(Integer imageId);
	
}
