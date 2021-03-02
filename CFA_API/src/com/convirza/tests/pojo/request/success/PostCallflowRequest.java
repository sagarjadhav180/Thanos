package com.convirza.tests.pojo.request.success;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.convirza.constants.TestDataYamlConstants;
import com.convirza.core.utils.RandomContentGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PostCallflowRequest {
	private String call_flow_status;
	private Boolean voicemail_enabled;
	private Integer ring_delay;
	private String default_ringto;
	private Boolean whisper_enabled;
	private String play_disclaimer;
	private String created_at;
	private PhoneNumber number;
	private String updated_at;
	private String whisper_message;
	private String call_flow_created;
	private Boolean postcall_ivr_enabled;
	private Integer repeat_interval;
	private String routable_type;
	private Integer campaign_id;
	private Boolean message_enabled;
	private Integer postcall_ivr_id;
	private String referral_number;
	private String message;
	private Integer call_value;
	private String record_until;
	private String email_to_notify;
	private Integer spam_threshold;
	private Boolean spam_filter_enabled;
	private Integer call_flow_id;
	private Boolean webhook_enabled;
	private String call_flow_name;
	private Integer group_id;
	private Integer channel_id;
	private boolean isSimultaneous;
	private Integer voicemail_rings;
	
	private List<OverflowNumbers> overflowNumbers;	

	public String getCall_flow_status ()
	{
		return call_flow_status;
	}

	public void setCall_flow_status (String call_flow_status)
	{
		this.call_flow_status = call_flow_status;
	}

	public Boolean getVoicemail_enabled ()
	{
		return voicemail_enabled;
	}

	public void setVoicemail_enabled (Boolean voicemail_enabled)
	{
		this.voicemail_enabled = voicemail_enabled;
	}

	public Integer getRing_delay ()
	{
		return ring_delay;
	}

	public void setRing_delay (Integer ring_delay)
	{
		this.ring_delay = ring_delay;
	}

	public String getDefault_ringto ()
	{
		return default_ringto;
	}

	public void setDefault_ringto (String default_ringto)
	{
		this.default_ringto = default_ringto;
	}

	public Boolean getWhisper_enabled ()
	{
		return whisper_enabled;
	}

	public void setWhisper_enabled (Boolean whisper_enabled)
	{
		this.whisper_enabled = whisper_enabled;
	}

	public String getPlay_disclaimer ()
	{
		return play_disclaimer;
	}

	public void setPlay_disclaimer (String play_disclaimer)
	{
		this.play_disclaimer = play_disclaimer;
	}

	public String getCreated_at ()
	{
		return created_at;
	}

	public void setCreated_at (String created_at)
	{
		this.created_at = created_at;
	}

	public PhoneNumber getNumber ()
	{
		return number;
	}

	public void setNumber (PhoneNumber number)
	{
		this.number = number;
	}

	public String getUpdated_at ()
	{
		return updated_at;
	}

	public void setUpdated_at (String updated_at)
	{
		this.updated_at = updated_at;
	}

	public String getWhisper_message ()
	{
		return whisper_message;
	}

	public void setWhisper_message (String whisper_message)
	{
		this.whisper_message = whisper_message;
	}

	public String getCall_flow_created ()
	{
		return call_flow_created;
	}

	public void setCall_flow_created (String call_flow_created)
	{
		this.call_flow_created = call_flow_created;
	}

	public Boolean getPostcall_ivr_enabled ()
	{
		return postcall_ivr_enabled;
	}

	public void setPostcall_ivr_enabled (Boolean postcall_ivr_enabled)
	{
		this.postcall_ivr_enabled = postcall_ivr_enabled;
	}

	public Integer getRepeat_interval ()
	{
		return repeat_interval;
	}

	public void setRepeat_interval (Integer repeat_interval)
	{
		this.repeat_interval = repeat_interval;
	}

	public String getRoutable_type ()
	{
		return routable_type;
	}

	public void setRoutable_type (String routable_type)
	{
		this.routable_type = routable_type;
	}

	public Integer getCampaign_id ()
	{
		return campaign_id;
	}

	public void setCampaign_id (Integer campaign_id)
	{
		this.campaign_id = campaign_id;
	}

	public Boolean getMessage_enabled ()
	{
		return message_enabled;
	}

	public void setMessage_enabled (Boolean message_enabled)
	{
		this.message_enabled = message_enabled;
	}

	public Integer getPostcall_ivr_id ()
	{
		return postcall_ivr_id;
	}

	public void setPostcall_ivr_id (Integer postcall_ivr_id)
	{
		this.postcall_ivr_id = postcall_ivr_id;
	}

	public String getReferral_number ()
	{
		return referral_number;
	}

	public void setReferral_number (String referral_number)
	{
		this.referral_number = referral_number;
	}

	public String getMessage ()
	{
		return message;
	}

	public void setMessage (String message)
	{
		this.message = message;
	}

	public Integer getCall_value ()
	{
		return call_value;
	}

	public void setCall_value (Integer call_value)
	{
		this.call_value = call_value;
	}

	public String getRecord_until ()
	{
		return record_until;
	}

	public void setRecord_until (String record_until)
	{
		this.record_until = record_until;
	}

	public String getEmail_to_notify ()
	{
		return email_to_notify;
	}

	public void setEmail_to_notify (String email_to_notify)
	{
		this.email_to_notify = email_to_notify;
	}

	public Integer getSpam_threshold ()
	{
		return spam_threshold;
	}

	public void setSpam_threshold (Integer spam_threshold)
	{
		this.spam_threshold = spam_threshold;
	}

	public Boolean getSpam_filter_enabled ()
	{
		return spam_filter_enabled;
	}

	public void setSpam_filter_enabled (Boolean spam_filter_enabled)
	{
		this.spam_filter_enabled = spam_filter_enabled;
	}

	public Integer getCall_flow_id ()
	{
		return call_flow_id;
	}

	public void setCall_flow_id (Integer call_flow_id)
	{
		this.call_flow_id = call_flow_id;
	}

	public Boolean getWebhook_enabled ()
	{
		return webhook_enabled;
	}

	public void setWebhook_enabled (Boolean webhook_enabled)
	{
		this.webhook_enabled = webhook_enabled;
	}

	public String getCall_flow_name ()
	{
		return call_flow_name;
	}

	public void setCall_flow_name (String call_flow_name)
	{
		this.call_flow_name = call_flow_name;
	}

	public Integer getGroup_id ()
	{
		return group_id;
	}

	public void setGroup_id (Integer group_id)
	{
		this.group_id = group_id;
	}

	public Integer getChannel_id ()
	{
		return channel_id;
	}

	public void setChannel_id (Integer channel_id)
	{
		this.channel_id = channel_id;
	}

	public boolean getIsSimultaneous() {
		return isSimultaneous;
	}

	public void setIsSimultaneous(boolean isSimultaneous) {
		this.isSimultaneous = isSimultaneous;
	}

	public List<OverflowNumbers> getOverflowNumbers() {
		return overflowNumbers;
	}

	public void setOverflowNumbers(List<OverflowNumbers> overflowNumbers) {
		this.overflowNumbers = overflowNumbers;
	}
	
	public Map<String,Object> getMapObject() {
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		map.put(TestDataYamlConstants.CallflowConstants.CALL_FLOW_ID, call_flow_id);
		map.put(TestDataYamlConstants.CallflowConstants.CALL_FLOW_NAME, call_flow_name);
		map.put(TestDataYamlConstants.CallflowConstants.CALL_FLOW_CREATED, call_flow_created);
		map.put(TestDataYamlConstants.CallflowConstants.CALL_FLOW_STATUS, call_flow_status);
		map.put(TestDataYamlConstants.CallflowConstants.REPEAT_INTERVAL, repeat_interval);
		map.put(TestDataYamlConstants.CallflowConstants.CALL_VALUE, call_value);
		map.put(TestDataYamlConstants.CallflowConstants.CHANNEL_ID, channel_id);
		map.put(TestDataYamlConstants.CallflowConstants.MESSAGE_ENABLED, message_enabled);
		map.put(TestDataYamlConstants.CallflowConstants.MESSAGE, message);
		map.put(TestDataYamlConstants.CallflowConstants.DEFAULT_RINGTO, default_ringto);
		map.put(TestDataYamlConstants.CallflowConstants.GROUP_ID, group_id);
		map.put(TestDataYamlConstants.CallflowConstants.EMAIL_TO_NOTIFY, email_to_notify);
		map.put(TestDataYamlConstants.CallflowConstants.PLAY_DISCLAIMER, play_disclaimer);
		map.put(TestDataYamlConstants.CallflowConstants.CREATED_AT, created_at);
		map.put(TestDataYamlConstants.CallflowConstants.UPDATED_AT, updated_at);
		map.put(TestDataYamlConstants.CallflowConstants.VOICEMAIL_ENABLED, voicemail_enabled);
		map.put(TestDataYamlConstants.CallflowConstants.ROUTABLE_TYPE, routable_type);
		map.put(TestDataYamlConstants.CallflowConstants.WEBHOOK_ENABLED, webhook_enabled);
		map.put(TestDataYamlConstants.CallflowConstants.RECORD_UNTIL, record_until);
		map.put(TestDataYamlConstants.CallflowConstants.WHISPER_ENABLED, whisper_enabled);
		map.put(TestDataYamlConstants.CallflowConstants.WHISPER_MESSAGE, whisper_message);
		map.put(TestDataYamlConstants.CallflowConstants.RING_DELAY, ring_delay);
		map.put(TestDataYamlConstants.CallflowConstants.POSTCALL_IVR_ENABLED, postcall_ivr_enabled);
		map.put(TestDataYamlConstants.CallflowConstants.POSTCALL_IVR_ID, postcall_ivr_id);
		map.put(TestDataYamlConstants.CallflowConstants.SPAM_THRESHOLD, spam_threshold);
		map.put(TestDataYamlConstants.CallflowConstants.SPAM_FILTER_ENABLED, spam_filter_enabled);
		map.put(TestDataYamlConstants.CallflowConstants.REFERRAL_NUMBER, referral_number);
		map.put(TestDataYamlConstants.CallflowConstants.CAMPAIGN_ID, campaign_id);
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> phoneNumber = 
		    mapper.convertValue(number, new TypeReference<Map<String, Object>>() {});
		map.put(TestDataYamlConstants.CallflowConstants.NUMBER, phoneNumber);
		return map;
	}
	
	@Override
	public String toString()
	{
		return "ClassPojo [call_flow_status = "+call_flow_status+", voicemail_enabled = "+voicemail_enabled+", ring_delay = "+ring_delay+", default_ringto = "+default_ringto+", whisper_enabled = "+whisper_enabled+", play_disclaimer = "+play_disclaimer+", created_at = "+created_at+", number = "+number+", updated_at = "+updated_at+", whisper_message = "+whisper_message+", call_flow_created = "+call_flow_created+", postcall_ivr_enabled = "+postcall_ivr_enabled+", repeat_interval = "+repeat_interval+", routable_type = "+routable_type+", campaign_id = "+campaign_id+", message_enabled = "+message_enabled+", postcall_ivr_id = "+postcall_ivr_id+", referral_number = "+referral_number+", message = "+message+", call_value = "+call_value+", record_until = "+record_until+", email_to_notify = "+email_to_notify+", spam_threshold = "+spam_threshold+", spam_filter_enabled = "+spam_filter_enabled+", call_flow_id = "+call_flow_id+", webhook_enabled = "+webhook_enabled+", call_flow_name = "+call_flow_name+", group_id = "+group_id+", channel_id = "+channel_id+"]";
	}

	public Integer getVoicemail_rings() {
		return voicemail_rings;
	}

	public void setVoicemail_rings(Integer voicemail_rings) {
		this.voicemail_rings = voicemail_rings;
	}

}
