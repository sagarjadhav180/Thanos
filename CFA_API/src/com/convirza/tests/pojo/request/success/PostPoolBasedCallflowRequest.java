package com.convirza.tests.pojo.request.success;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.convirza.constants.TestDataYamlConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class PostPoolBasedCallflowRequest {
	
	private String call_flow_name;
	private String call_flow_created;
	private String call_flow_status;
	private Integer repeat_interval;
	private Integer call_value;
	private Integer channel_id;
	private boolean message_enabled;
	private String message;
	private String default_ringto;
	private Integer group_id;
	private String email_to_notify;
	private String play_disclaimer;
	private String created_at = null;
	private String updated_at = null;
	private boolean voicemail_enabled;
	private Integer voicemail_rings;
	private String routable_type;
	private boolean webhook_enabled;
	private String record_until = null;
	private boolean whisper_enabled;
	private String whisper_message;
	private Integer ring_delay;
	private boolean postcall_ivr_enabled;
	private Integer postcall_ivr_id;
	private Integer spam_threshold;
	private boolean spam_filter_enabled;
	private String referral_number = null;
	private boolean isSimultaneous;
	private Integer campaign_id;
	private Integer call_flow_id;
	private NumberPool number_pool;
	private List<OverflowNumbers> overflowNumbers = null;

	
	public Integer getCall_flow_id() {
		return call_flow_id;
	}

	public void setCall_flow_id(Integer call_flow_id) {
		this.call_flow_id = call_flow_id;
	} 

	// Getter Methods 

	public String getCall_flow_name() {
		return call_flow_name;
	}

	public String getCall_flow_created() {
		return call_flow_created;
	}

	public String getCall_flow_status() {
		return call_flow_status;
	}

	public Integer getRepeat_interval() {
		return repeat_interval;
	}

	public Integer getCall_value() {
		return call_value;
	}

	public Integer getChannel_id() {
		return channel_id;
	}

	public boolean getMessage_enabled() {
		return message_enabled;
	}

	public String getMessage() {
		return message;
	}

	public String getDefault_ringto() {
		return default_ringto;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public String getEmail_to_notify() {
		return email_to_notify;
	}

	public String getPlay_disclaimer() {
		return play_disclaimer;
	}

	public String getCreated_at() {
		return created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public boolean getVoicemail_enabled() {
		return voicemail_enabled;
	}

	public Integer getVoicemail_rings() {
		return voicemail_rings;
	}

	public String getRoutable_type() {
		return routable_type;
	}

	public boolean getWebhook_enabled() {
		return webhook_enabled;
	}

	public String getRecord_until() {
		return record_until;
	}

	public boolean getWhisper_enabled() {
		return whisper_enabled;
	}

	public String getWhisper_message() {
		return whisper_message;
	}

	public Integer getRing_delay() {
		return ring_delay;
	}

	public boolean getPostcall_ivr_enabled() {
		return postcall_ivr_enabled;
	}

	public Integer getPostcall_ivr_id() {
		return postcall_ivr_id;
	}

	public Integer getSpam_threshold() {
		return spam_threshold;
	}

	public boolean getSpam_filter_enabled() {
		return spam_filter_enabled;
	}

	public String getReferral_number() {
		return referral_number;
	}

	public boolean getIsSimultaneous() {
		return isSimultaneous;
	}

	public Integer getCampaign_id() {
		return campaign_id;
	}

	public NumberPool getNumber_pool() {
		return number_pool;
	}

	// Setter Methods 

	public void setCall_flow_name(String call_flow_name) {
		this.call_flow_name = call_flow_name;
	}

	public void setCall_flow_created(String call_flow_created) {
		this.call_flow_created = call_flow_created;
	}

	public void setCall_flow_status(String call_flow_status) {
		this.call_flow_status = call_flow_status;
	}

	public void setRepeat_interval(Integer repeat_interval) {
		this.repeat_interval = repeat_interval;
	}

	public void setCall_value(Integer call_value) {
		this.call_value = call_value;
	}

	public void setChannel_id(Integer channel_id) {
		this.channel_id = channel_id;
	}

	public void setMessage_enabled(boolean message_enabled) {
		this.message_enabled = message_enabled;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setDefault_ringto(String default_ringto) {
		this.default_ringto = default_ringto;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	public void setEmail_to_notify(String email_to_notify) {
		this.email_to_notify = email_to_notify;
	}

	public void setPlay_disclaimer(String play_disclaimer) {
		this.play_disclaimer = play_disclaimer;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public void setVoicemail_enabled(boolean voicemail_enabled) {
		this.voicemail_enabled = voicemail_enabled;
	}

	public void setVoicemail_rings(Integer voicemail_rings) {
		this.voicemail_rings = voicemail_rings;
	}

	public void setRoutable_type(String routable_type) {
		this.routable_type = routable_type;
	}

	public void setWebhook_enabled(boolean webhook_enabled) {
		this.webhook_enabled = webhook_enabled;
	}

	public void setRecord_until(String record_until) {
		this.record_until = record_until;
	}

	public void setWhisper_enabled(boolean whisper_enabled) {
		this.whisper_enabled = whisper_enabled;
	}

	public void setWhisper_message(String whisper_message) {
		this.whisper_message = whisper_message;
	}

	public void setRing_delay(Integer ring_delay) {
		this.ring_delay = ring_delay;
	}

	public void setPostcall_ivr_enabled(boolean postcall_ivr_enabled) {
		this.postcall_ivr_enabled = postcall_ivr_enabled;
	}

	public void setPostcall_ivr_id(Integer postcall_ivr_id) {
		this.postcall_ivr_id = postcall_ivr_id;
	}

	public void setSpam_threshold(Integer spam_threshold) {
		this.spam_threshold = spam_threshold;
	}

	public void setSpam_filter_enabled(boolean spam_filter_enabled) {
		this.spam_filter_enabled = spam_filter_enabled;
	}

	public void setReferral_number(String referral_number) {
		this.referral_number = referral_number;
	}

	public void setIsSimultaneous(boolean isSimultaneous) {
		this.isSimultaneous = isSimultaneous;
	}

	public void setCampaign_id(Integer campaign_id) {
		this.campaign_id = campaign_id;
	}

	public List<OverflowNumbers> getOverflowNumbers() {
		return overflowNumbers;
	}

	public void setOverflowNumbers(List<OverflowNumbers> overflowNumbers) {
		this.overflowNumbers = overflowNumbers;
	}

	public void setNumber_pool(NumberPool number_poolObject) {
		this.number_pool = number_poolObject;
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
		    mapper.convertValue(number_pool, new TypeReference<Map<String, Object>>() {});
		map.put(TestDataYamlConstants.CallflowConstants.NUMBER_POOL, phoneNumber);
		return map;
	}

	@Override
	public String toString() {
		return "PostPoolBasedCallflowRequest [call_flow_name=" + call_flow_name + ", call_flow_created="
				+ call_flow_created + ", call_flow_status=" + call_flow_status + ", repeat_interval="
				+ repeat_interval + ", call_value=" + call_value + ", channel_id=" + channel_id
				+ ", message_enabled=" + message_enabled + ", message=" + message + ", default_ringto=" + default_ringto
				+ ", group_id=" + group_id + ", email_to_notify=" + email_to_notify + ", play_disclaimer="
				+ play_disclaimer + ", created_at=" + created_at + ", updated_at=" + updated_at + ", voicemail_enabled="
				+ voicemail_enabled + ", voicemail_rings=" + voicemail_rings + ", routable_type=" + routable_type
				+ ", webhook_enabled=" + webhook_enabled + ", record_until=" + record_until + ", whisper_enabled="
				+ whisper_enabled + ", whisper_message=" + whisper_message + ", ring_delay=" + ring_delay
				+ ", postcall_ivr_enabled=" + postcall_ivr_enabled + ", postcall_ivr_id=" + postcall_ivr_id
				+ ", spam_threshold=" + spam_threshold + ", spam_filter_enabled=" + spam_filter_enabled
				+ ", referral_number=" + referral_number + ", isSimultaneous=" + isSimultaneous + ", campaign_id="
				+ campaign_id + ", call_flow_id=" + call_flow_id + ", number_pool=" + number_pool
				+ ", overflowNumbers=" + overflowNumbers + "]";
	}


	
	
}
