/* SiteCatalyst code version: H.14. Copyright Omniture, Inc. More info available at http://www.omniture.com */
/* Author: Neil Evans */
/************************** CONFIG SECTION ****************************************/
/* Specify the Report Suite(s) */
var s_account="sundevdevforums";
var sun_dynamicAccountSelection=true;
var sun_dynamicAccountList="sunglobal,sundevforums=forums.sun.com;sundevdevforums=.";	
/* Specify the Report Suite ID */
var s_siteid = "sunforums:";
/* Site Settings */
if(typeof s_prop23=='undefined'){var s_prop23=""};
   s_prop23=document.title;
if(typeof s_channel=='undefined'){var s_channel=""};
   s_channel=s_siteid;
/* Remote Omniture JS call  */
var sun_ssl=(window.location.protocol.toLowerCase().indexOf("https")!=-1);
	if(sun_ssl == true) { var fullURL = "https://www.sun.com/share/metrics/metrics_group1.js"; }
		else { var fullURL= "http://www-cdn.sun.com/share/metrics/metrics_group1.js"; }
document.write("<sc" + "ript type=\"text/javascript\" src=\""+fullURL+"\"></sc" + "ript>");
/************************** END CONFIG SECTION **************************************/
/* CUSTOM VARS PULLED FROM QUERYSTRING */
/* prePlugins functions */
function s_prePlugins(s) {
	/* grab categoryID param */
	s.prop18=s.getQueryParam('categoryID');
	s_prop18=s.prop18;
	/* grab forumID param */
	s.prop19=s.getQueryParam('forumID');
	s_prop19=s.prop19;
	/* grab threadID param */
	s.prop20=s.getQueryParam('threadID');
	s_prop20=s.prop20;
	/* grab messageID param */
	s.prop21=s.getQueryParam('messageID');
	s_prop21=s.prop21;
	/* grab userID param */
	s.prop22=s.getQueryParam('userID');
	s_prop22=s.prop22;
}