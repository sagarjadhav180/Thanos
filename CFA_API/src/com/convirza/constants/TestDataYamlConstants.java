package com.convirza.constants;

public class TestDataYamlConstants {

	public static final String CONVIRZA = "convirza";
	public static final String GROUP = "group";
	public static final String USER = "user";
	public static final String CAMPAIGN = "campaign";
	public static final String CALLFLOW = "callflow";
	public static final String WEBHOOK = "webhook";
	public static final String CUSTOM_SOURCE = "custom_source";
	public static final String BLACKLISTED_NUMBER = "blacklisted_number";
	
	public static class GroupConstants {
		public static final String GROUP_ID = "group_id";
		public static final String GROUP_NAME = "group_name";
		public static final String GROUP_PARENT_ID = "group_parent_id";
		public static final String TOP_GROUP_ID = "top_group_id";
		public static final String BILLING_ID = "billing_id";
		public static final String GROUP_EXT_ID = "group_ext_id";
		public static final String ADDRESS = "address";
		public static final String CITY = "city";
		public static final String STATE = "state";
		public static final String ZIP = "zip";
		public static final String PHONE_NUMBER = "phone_number";
		public static final String INDUSTRY_ID = "industry_id";
		public static final String GROUP_CREATED = "group_created";
		public static final String GROUP_MODIFIED = "group_modified";

		public static class User {
			public static final String CT_USER_ID = "ct_user_id";
			public static final String USER_EMAIL = "user_email";
			public static final String USER_TITLE = "user_title";
			public static final String ROLE_ID = "role_id";
			public static final String USER_STATUS = "user_status";
		}
	}
	
	public static class UserConstants {
		public static final String FIRST_NAME = "first_name";
		public static final String LAST_NAME = "last_name";
		public static final String ID = "id";
		public static final String EMAIL = "email";
		public static final String PHONE_NUMBER = "phone_number";
		public static final String USER_EXT_ID = "user_ext_id";
		public static final String ROLE = "role";
		public static final String STATUS = "status";
		public static final String GROUP_ID = "group_id";
	}
	
	public static class CampaignConstants {
		public static final String CAMPAIGN_ID = "campaign_id";
		public static final String CAMPAIGN_NAME = "campaign_name";
		public static final String CAMPAIGN_EXT_ID = "campaign_ext_id";
		public static final String CAMPAIGN_STATUS = "campaign_status";
		public static final String CAMPAIGN_CREATED = "campaign_created";
		public static final String CAMPAIGN_MODIFIED = "campaign_modified";
		public static final String CAMPAIGN_START_DATE = "campaign_start_date";
		public static final String CAMPAIGN_END_DATE = "campaign_end_date";
		public static final String CAMPAIGN_OWNER_USER_ID = "campaign_owner_user_id";
		public static final String GROUP_ID = "group_id";
		public static final String CAMPAIGN_USERS = "campaign_users";
	}
	
	public static class CallflowConstants {
		public static final String CALL_FLOW_ID = "call_flow_id";
		public static final String CALL_FLOW_NAME = "call_flow_name";
		public static final String CALL_FLOW_CREATED = "call_flow_created";
		public static final String CALL_FLOW_STATUS = "call_flow_status";
		public static final String REPEAT_INTERVAL = "repeat_interval";
		public static final String CALL_VALUE = "call_value";
		public static final String CHANNEL_ID = "channel_id";
		public static final String MESSAGE_ENABLED = "message_enabled";
		public static final String MESSAGE = "message";
		public static final String DEFAULT_RINGTO = "default_ringto";
		public static final String GROUP_ID = "group_id";
		public static final String EMAIL_TO_NOTIFY = "email_to_notify";
		public static final String PLAY_DISCLAIMER = "play_disclaimer";
		public static final String CREATED_AT = "created_at";
		public static final String UPDATED_AT = "updated_at";
		public static final String VOICEMAIL_ENABLED = "voicemail_enabled";
		public static final String ROUTABLE_TYPE = "routable_type";
		public static final String WEBHOOK_ENABLED = "webhook_enabled";
		public static final String RECORD_UNTIL = "record_until";
		public static final String WHISPER_ENABLED = "whisper_enabled";
		public static final String WHISPER_MESSAGE = "whisper_message";
		public static final String RING_DELAY = "ring_delay";
		public static final String POSTCALL_IVR_ENABLED = "postcall_ivr_enabled";
		public static final String POSTCALL_IVR_ID = "postcall_ivr_id";
		public static final String SPAM_THRESHOLD = "spam_threshold";
		public static final String SPAM_FILTER_ENABLED = "spam_filter_enabled";
		public static final String REFERRAL_NUMBER = "referral_number";
		public static final String CAMPAIGN_ID = "campaign_id";
		public static final String NUMBER = "number";
		public static final String NUMBER_POOL = "number_pool";
		public static final String ISSIMULTANEOUS = "isSimultaneous";
		public static final String OVERFLOWNUMBERS = "overflowNumbers";
	}
	
	public static class PhoneNumber {
		public static final String PHONE_NUMBER_ID = "phone_number_id";
		public static final String PHONE_NUMBER = "phone_number";
	}
	
	public static class WebhookConstants {
		public static final String WEBHOOK_ID = "webhook_id";
		public static final String WEBHOOK_STATUS = "webhook_status";
		public static final String ORG_UNIT_ID = "org_unit_id";
		public static final String WEBHOOK_NAME = "webhook_Name";
		public static final String DESCRIPTION = "description";
		public static final String WEBHOOK_ENDPOINT = "webhook_Endpoint";
		public static final String METHOD = "method";
		public static final String FORMAT = "format";
		public static final String INCLUDE_DNI_LOGS = "include_DNI_Logs";
		public static final String INCLUDE_INDICATOR_SCORES = "include_Indicator_Scores";
		public static final String STATIC_PARAMETER = "static_Parameter";
		
		public static class staticParameters {
			public static final String FIELD_NAME = "field_Name";
			public static final String FIELD_VALUE = "field_Value";
		}
	}
	
	public static class CustomSourceConstants {
		public static final String ORG_UNIT_ID = "org_unit_id";
		public static final String CUSTOM_SOURCE_NAME = "custom_source_name";
		public static final String CUSTOM_SOURCE_ID = "custom_source_id";
	}

	public static class IndustryConstants {
		public static final String INDUSTRY_NAME = "industry_name";
	}
	
	public static class BlacklistNumberConstants {
		public static final String BLACKLISTED_NUMBER = "number";
	}
}
