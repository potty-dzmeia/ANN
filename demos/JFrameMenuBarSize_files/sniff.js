/* ###########################################################################

GLOBAL ASSETS RELEASE v3.0

SUN SNIFF & COMMON JS LIB v5.9

BUILD DATE: 20070817

COPYRIGHT SUN MICROSYSTEMS INC. 2007

CONTACT webdesign -at- sun.com WITH ANY QUESTIONS

########################################################################### */

is = new ottosniff();
function ottosniff(){
	var b = navigator.appName
	if (b=="Netscape") this.b = "ns"
	else this.b = b
	this.version = navigator.appVersion
	this.v = parseInt(this.version)
	this.ns = (this.b=="ns" && this.v>=5)
	this.op = (navigator.userAgent.indexOf('Opera')>-1)
	this.safari = (navigator.userAgent.indexOf('Safari')>-1)
	this.op7 = (navigator.userAgent.indexOf('Opera')>-1 && this.v>=7 && this.v<8)
	this.op78 = (navigator.userAgent.indexOf('Opera')>-1 && this.v>=7 || navigator.userAgent.indexOf('Opera')>-1 && this.v>=8)
	this.ie5 = (this.version.indexOf('MSIE 5')>-1)
	this.ie6 = (this.version.indexOf('MSIE 6')>-1)
	this.ie7 = (this.version.indexOf('MSIE 7')>-1)
	this.ie56 = (this.ie5||this.ie6)
	this.ie567 = (this.ie5||this.ie6||this.ie7)
	this.iewin = (this.ie56 && navigator.userAgent.indexOf('Windows')>-1 || this.ie7 && navigator.userAgent.indexOf('Windows')>-1)
	this.iemac = (this.ie56 && navigator.userAgent.indexOf('Mac')>-1)
	this.moz = (navigator.userAgent.indexOf('Mozilla')>-1)
	this.ff = (navigator.userAgent.indexOf('Firefox')>-1)
	this.moz13 = (navigator.userAgent.indexOf('Mozilla')>-1 && navigator.userAgent.indexOf('1.3')>-1)
	this.oldmoz = (navigator.userAgent.indexOf('SunOS')>-1 || this.moz13 && !this.ff ||  navigator.userAgent.indexOf('Mozilla')>-1 && navigator.userAgent.indexOf('1.4')>-1 && !this.ff || navigator.userAgent.indexOf('Mozilla')>-1 && navigator.userAgent.indexOf('1.5')>-1 && !this.ff || navigator.userAgent.indexOf('Mozilla')>-1 && navigator.userAgent.indexOf('1.6')>-1 && !this.ff)
	this.anymoz = (this.ff||this.moz)
	this.ns6 = (navigator.userAgent.indexOf('Netscape6')>-1)
	this.docom = (this.ie56||this.ns||this.iewin||this.op||this.iemac||this.safari||this.moz||this.oldmoz||this.ns6)
}

// VARS
ptest="noprint";
var oldmenu = new Array();
var navmenu = new Array();
var a1menus = new Array();
var a1hrefs = new Array();
var imgpreload = new Array();
var imgpostload = new Array();
var preloaderOn = new Array();
var preloaderOff = new Array();
var preloaderActive = new Array();
var activeImg = new Array();
var plx = 0;

// ADD BROWSER CLASS TO HTML TAG
if (document.getElementsByTagName('html')[0]){
	var bclass = "";
	if(is.op){var bclass = "browserOpera ";}
	else if(is.safari){var bclass = "browserSafari ";}
	else if(is.ie56){var bclass = "browserExplorer56 browserExplorer ";}
	else if(is.ie7){var bclass = "browserExplorer7 browserExplorer ";}
	else if(is.iemac){var bclass = "browserExplorerMac ";}
	else if(is.oldmoz){var bclass = "browserOldMoz ";}
	else {var bclass = "";}
	bclass = bclass+"jsenabled";
	if (typeof a1menuwrap != 'undefined' && a1menuwrap){bclass = bclass+" hasA1menus";}
	addClassName(document.getElementsByTagName('html')[0], bclass);
}

// sniff for various page elements
var kdoc = document;
var sniffStatus = {'a1':false,'pagetitle':false,'sunhome':false}

// PAGE PREP
function prepSunPage(){
	if (is.docom){
		// sunhome test & omni test
		if(kdoc.getElementById('a0v2') && window.s_account){
			sniffStatus.sunhome = true;
		}
		//copyright
		if(kdoc.getElementById('copyDate')){
			var thisdate = new Date();
			kdoc.getElementById('copyDate').innerHTML = "1994-"+thisdate.getFullYear();
		}
		// no hardcode A2
		if (ptest.indexOf("yesprint") == -1 && ptest.indexOf("prepmenus") == -1 && document.getElementById('mtopic1') && navmenu['1.0']){
			printmenus();
			prepmenus();
		}
		// add actions to global search
		if (kdoc.getElementById('searchfield')){
			kdoc.getElementById('searchfield').onfocus = function(){
				if(kdoc.getElementById('searchfield').value==kdoc.getElementById('searchfield').defaultValue)kdoc.getElementById('searchfield').value='';
				if (!is.iemac){
					kdoc.getElementById('searchfield').style.width='110px';
				}
			};
			kdoc.getElementById('searchfield').onblur = function(){
				if(kdoc.getElementById('searchfield').value=="")kdoc.getElementById('searchfield').value=kdoc.getElementById('searchfield').defaultValue;
				if (!is.iemac){
					kdoc.getElementById('searchfield').style.width='67px'
				}
			};
		}
		// add blur action to logo
		if (kdoc.getElementById('sunlogo')){
			kdoc.getElementById('sunlogo').onfocus = function(){hideA2(0)};
		}
		// add bg spacer gif to off divs in IE for better response
		if(is.ie56 && kdoc.getElementById('offdiv') && kdoc.getElementById('offdivL') && kdoc.getElementById('offdivT') && kdoc.getElementById('offdivR') && imdir && !is.iemac){
			kdoc.getElementById('offdiv').style.background = kdoc.getElementById('offdivL').style.background = kdoc.getElementById('offdivT').style.background = kdoc.getElementById('offdivR').style.background = 'url('+imdir+'/a.gif)';
		}
		//dom crawl
		domCrawl(kdoc);
		// prep homepage
		if (kdoc.getElementById('newsitem2') || kdoc.getElementById('subhover2') || kdoc.getElementById('a0v2')){
			prephome();
			done = true;
		}
		if (typeof postCrawl != 'undefined') {
			for (func in postCrawl) { postCrawl[func](); }
		}
	}
}

//DOM CRAWL
function domCrawl(domObject,tagList){
	if (is.ie5 && !tagList){
		 var tagList = new Array('a','b','div','span','td','li','ul','input','select','img','option','area','a','div','span');
	}else if (!tagList){
		 var tagList = new Array('*');
	}else if (tagList){
		 var tagList = tagList.split(',');
	}
	for (var ivp=0;ivp<tagList.length;ivp++){
		var an = domObject.getElementsByTagName(tagList[ivp]);
		for (var i=0;i<an.length;i++){
			var lcNodeName = an[i].nodeName.toLowerCase();
			
			if (an[i].className.indexOf('cTool-') > -1){
				sniffClassTool(an[i]);
			}

			if (lcNodeName == 'img' && an[i].src.indexOf('_off.') > -1){
				sniffRollover(an[i]);
			}else if(!an[i].className){
				// do nothing, thus preventing much unnecessary checking
			}else if (an[i].className.indexOf('k2ajax-') > -1){
				sniffK2ajax(an[i]);
			}else if (an[i].className.indexOf('k2over') > -1 || an[i].className.indexOf('k2focus') > -1 || an[i].className.indexOf('k2cl') > -1 || an[i].className.indexOf('a2menu') > -1 || an[i].className.indexOf('k2show') > -1 || an[i].className.indexOf('k2hide') > -1){
				sniffK2(an[i]);
			}else if (sniffStatus.sunhome == true && an[i].className.indexOf('tickeritem') > -1){
				sniffTicker(an[i]);
			}else if (lcNodeName == 'div' && hasClassName(an[i], 'g23')){
				sniffG23(an[i]);
			}else if (lcNodeName == 'a' && hasClassName(an[i], 'media-launch')){
				sniffMedia(an[i]);
			}else if (hasClassName(an[i], 'modal-launch') || hasClassName(an[i], 'modal-close')){
				sniffModal(an[i]);
			}else if (sniffStatus.pagetitle == false && lcNodeName == 'div' && (hasClassName(an[i],'pagetitle') || hasClassName(an[i],'smallpagetitle')) && !hasClassName(document.body,'a0v3')){
				sniffStatus.pagetitle = true; sniffSharePage(an[i]);
			}else if (lcNodeName == 'input' && hasClassName(an[i], 'autoclear')){
				sniffAutoclear(an[i]);
			}else if (lcNodeName == 'img' && hasClassName(an[i], 'spriteswap')){
				sniffSpriteSwap(an[i]);
			}else if (lcNodeName == 'div' && hasClassName(an[i], 'g27w2')){
				sniffG27(an[i]);
			}else if (lcNodeName == 'div' && hasClassName(an[i], 'imgbox')){
				sniffImgbox(an[i]);
			}else if (lcNodeName == 'ul' && hasClassName(an[i], 'selectTabs')){
				sniffSelectTab(an[i]);
			}else if (sniffStatus.a1 == false && lcNodeName == 'div' && hasClassName(an[i], 'a1r2')){
				sniffStatus.a1 = true; sniffA1(an[i]);
			}else if (lcNodeName == 'select' && hasClassName(an[i], 'goto') || lcNodeName == 'select' && hasClassName(an[i], 'showDiv')){
				sniffGoto(an[i]);
			}else if (lcNodeName == 'ul' && hasClassName(an[i], 'goto') || lcNodeName == 'ul' && hasClassName(an[i], 'showDiv')){
				sniffGotoUL(an[i]);
			}else if (hasClassName(an[i], 'xfadefirst')){
				sniffXfade(an[i]);
			}else if (lcNodeName == 'ul' && hasClassName(an[i], 'listfade')){
				sniffListfade(an[i]);
			}else if (lcNodeName == 'a' && hasClassName(an[i], 'popup') || lcNodeName == 'area' && hasClassName(an[i], 'popup')){
				sniffPopUp(an[i]);
			}else if (lcNodeName == 'a' && hasClassName(an[i], 'imgswap') || lcNodeName == 'area' && hasClassName(an[i], 'imgswap') || lcNodeName == 'img' && hasClassName(an[i], 'imgswap') || lcNodeName == 'span' && hasClassName(an[i], 'imgswap')){
				sniffImgswap(an[i]);
			}else if ((lcNodeName == 'a' || lcNodeName == 'area' || lcNodeName == 'span' || lcNodeName == 'img') && an[i].className.indexOf('mswap') > -1 ){
				sniffMultiswap(an[i]);
			}else if (lcNodeName == 'img' && hasClassName(an[i], 'postload')){
				imgpostload.push(an[i]);
			}else if (lcNodeName == 'a' && hasClassName(an[i], 'toggleObj') || lcNodeName == 'area' && hasClassName(an[i], 'toggleObj')){
				sniffToggler(an[i]);
			}else if (lcNodeName == 'a' && hasClassName(an[i], "toggle-all-table-checkboxes")){
				sniffToggleAllCheckboxesInTable(an[i]);
			}else if (lcNodeName == 'div' && hasClassName(an[i], "pc1collapsible")){
				sniffExpandCollapsePc1(an[i]);
			}else if (lcNodeName == 'form' && an[i].className.indexOf('wgform-') > -1){
				sniffFormHijax(an[i]);
			}else if (lcNodeName == 'a' && an[i].className.indexOf('hijax-') > -1 || lcNodeName == 'span' && an[i].className.indexOf('hijax-') > -1 || lcNodeName == 'ul' && an[i].className.indexOf('hijax-') > -1){
				sniffLinkHijax(an[i]);
			}else if (lcNodeName == 'span' && hasClassName(an[i],'site-select')){
				sniffSiteSelector(an[i]);
			}
			if(lcNodeName == 'a' && sniffStatus.sunhome == true || lcNodeName == 'area' && sniffStatus.sunhome == true || lcNodeName == 'input' && sniffStatus.sunhome == true){
				var fn = function(){ OmnitureSetObject(this) };
				addEvent(an[i],"click",fn);
			}
			if (typeof widgets != 'undefined') {
				if (hasClassName(an[i], 'wg1')){
					sniffWg1(an[i]);
				}
			}
		}
	}
	for (var imp=0;imp<imgpostload.length;imp++){
		if(imgpostload[imp].title){
			imgpostload[imp].src = imgpostload[imp].title;
			imgpostload[imp].title = "";
		}
	}
}

//GET PARENT
function hasParent(obj,tag,classname){
	var parent = obj;
	if(classname){
		while (parent = parent.parentNode) {
			if (parent.nodeName.toLowerCase() == tag && hasClassName(parent,classname) || tag == "*" && hasClassName(parent,classname)){
				return parent;
			}
		}
	}else{
		while (parent = parent.parentNode) {
			if (parent.id == tag){
				return parent;
			}
		}
	}
}


//MOVE OPENED
window.onresize = function moveK2(){
	for (kdp in ked){
		if (ked[kdp][0]){
			if (is.iemac){
				hideK2(ked[kdp][0]);
			}else{
				showK2(ked[kdp][0],ked[kdp][1],ked[kdp][2],ked[kdp][3],ked[kdp][4],ked[kdp][5],ked[kdp][6],ked[kdp][7]);
			}
		}
	}
	if(typeof flym != "undefined" && flym[1] && is.op){
		var fa = 0;
		while (flym[fa]){
			flym[fa].style.top = "-1500px";
			fa++;
		}
		flym[1000].style.top = "-1500px";
		flym[1001].style.top = "-1500px";
		flym[1002].style.top = "-1500px";
	}
}

// ADD PREPSUNPAGE ONLOAD
if (is.docom){
	if (window.attachEvent){
		window.attachEvent('onload',prepSunPage);
	}else if (window.addEventListener){
		window.addEventListener('load',prepSunPage,false);
	}else if (is.iemac){
		document.onreadystatechange = function(){if (document.readyState == "interactive"){prepSunPage()}};
	}
}

// ADD ONRESIZE EVENTS
function addOnresizeEvent(func){
  var oldrsize = window.onresize;
  if (typeof window.onresize != 'function'){
	window.onresize = func;
  }else {
	window.onresize = function(){
	  oldrsize();
	  func();
	}
  }
}

// ADD CLASSES TO OBJECTS
function addClassName(element, className){
	if (hasClassName(element, className)) { return false; }
	if (!element.className) { element.className = className; }
	else { element.className += ' '+className; }
	return true;
}

// REMOVE CLASSES FROM OBJECTS
function removeClassName(element, className){
	if (!hasClassName(element, className)) { return false; }
	var classNames = element.className.split(' ');
	var newClassNames = [];
	for (var a=0; a<classNames.length; a++){
		if (classNames[a] != className) { newClassNames[newClassNames.length] = classNames[a]; }
	}
	element.className = newClassNames.join(' ');
	return true;
}

// TEST FOR CLASS NAME
function hasClassName(element, className){
	var exp = new RegExp("(^|\\s)"+className+"($|\\s)");
	return (element.className && exp.exec(element.className))?true:false;
}

// GET ELEMENTS BY CLASS NAME
function getElementsByClassName(node, className){
	var results = [];
	var els = node.getElementsByTagName("*");
	var len=els.length;
	for(var a=0; a<len; a++){
		if(hasClassName(els[a], className)){
			results.push(els[a]);
		}
	}
	return results;
}

// GET FULL CLASS NAME FROM PARTIAL STRING
function getClassContains(obj,subst){
	var rcl = false;
	var cls = obj.className.split(' ');
	for (var v=0;v<cls.length;v++){
		if (cls[v].indexOf(subst) > -1){
			rcl = cls[v]; 
		}
	}
	return rcl;
}

// GET CHILD NODES VIA TAG NAME
function getChildNodesByTagName(el, tagName){
	var cn = el.childNodes;
	var nd = new Array();
	for (var n=0;n<cn.length;n++){
		if(tagName == cn[n].nodeName.toLowerCase()){
			nd.push(cn[n]);
		}
	}
	return nd;
}

// IS OBJECT AN ARRAY
function isArray(obj){
	return obj && typeof obj.unshift != 'undefined';
}

// SHORTCUT FOR BUILDING ELEMENTS
function elem(name, atts, content) {
	// name: a tag name, with optional class or id: 'div', 'div.foo', 'div#bar', 'div.foo#bar', 'div#bar.foo'
	// atts: optional. object where keys=attribute names, values=attribute values: {'href':'page.html','target':'_blank'}
	// content: optional. either a string, or an element, or an arry of strings or elements
	if (name.indexOf('.') + name.indexOf('#') > -2) {
	var className = (name.indexOf('.') > -1) ? name.replace(/^.*\.([^\.#]*).*$/,"$1") : "";
		var id = (name.indexOf('#') > -1) ? name.replace(/^.*#([^\.#]*).*$/,"$1") : "";
		name = name.replace(/^([^\.#]*).*$/,'$1');
	}
	var e = document.createElement(name);
	if (className) { e.className = className; }
	if (id) { e.id = id; }
	if (atts) {
		for (key in atts) {
			// setAttribute() has shaky support, try direct methods first
			if (key == 'class') { e.className = atts[key]; }
			else if (key == 'id') { e.id = atts[key]; }
			else if (key == 'href') { e.href = atts[key]; }
			else if (key == 'action') { e.action = atts[key]; }
			else if (key == 'method') { e.method = atts[key]; }
			else if (key == 'title') { e.title = atts[key]; }
			else if (key == 'alt') { e.alt = atts[key]; }
			else if (key == 'border') { e.border = atts[key]; }
			else if (key == 'caption') { e.caption = atts[key]; }
			else if (key == 'cellspacing') { e.cellspacing = atts[key]; }
			else if (key == 'for') { e.htmlFor = atts[key]; }
			else { e.setAttribute(key, atts[key]); }
		}
	}
	if (content) {
		if (!isArray(content)) { // it's not an array
			content = [content];
		}
		for (var a=0; a<content.length; a++) {
			if (typeof content[a] == 'string') {
				e.appendChild(document.createTextNode(content[a]));
			} else {
				e.appendChild(content[a]);
			}
		}
	}
	if (name.toLowerCase() == 'img' && !e.alt) { e.alt = ''; }
	return e;
}

// GRAB JUST THE TEXTUAL DATA OF AN ELEMENT
function elemText(el) {
	// <a id="foo" href="page.html">click <b>here</b></a>
	// elemText(document.getElementById('foo')) == "click here"
	// warning: recurses through *all* descendants of el
	var chlds = el.childNodes;
	var result = '';
	for (var a=0; a<chlds.length; a++) {
		if (3 == chlds[a].nodeType) {
			result += chlds[a].data;
		} else if (1 == chlds[a].nodeType) {
			result += elemText(chlds[a]);
		}
	}
	return result;
}

// FIND PREVIOUS ELEMENT
function prevElem(el) {
	var prev = el.previousSibling;
	for (var a=0; a<1000; a++) {
		if (!prev) { return null; }
		if (prev.nodeType==1) { return prev; }
		prev = prev.previousSibling;
	}
	throw "couldn't find previous sibling";
}

// FIND NEXT ELEMENT
function nextElem(el) {
	var next = el.nextSibling;
	for (var a=0; a<1000; a++) {
		if (!next) { return null; }
		if (next.nodeType==1) { return next; }
		next = next.nextSibling;
	}
	throw "couldn't find next sibling";
}

// SET OPACITY
function setopacity(id_or_obj,opac){
	if (document.getElementById(id_or_obj)){
		var oobj = document.getElementById(id_or_obj);
	}else if(id_or_obj){
		var oobj = id_or_obj;
	}
	if (oobj){
		if (document.all && !is.op){
			oobj.filters.alpha.opacity = opac * 100;
		}else{
			oobj.style.MozOpacity = opac;
			oobj.style.opacity = opac;
		}
	}
}


/*
	AddEvent()
	See <http://www.dustindiaz.com/rock-solid-addevent/> for more information.
	This software is licensed under the CC-GNU LGPL <http://creativecommons.org/licenses/LGPL/2.1/>
*/

//ADD EVENT
function addEvent( obj, type, fn ) {
	if (obj.addEventListener) {
		obj.addEventListener( type, fn, false );
		EventCache.add(obj, type, fn);
	}
	else if (obj.attachEvent) {
		obj["e"+type+fn] = fn;
		obj[type+fn] = function() { obj["e"+type+fn]( window.event ); }
		obj.attachEvent( "on"+type, obj[type+fn] );
		EventCache.add(obj, type, fn);
	}
	else {
		obj["on"+type] = obj["e"+type+fn];
	}
}

/*
	EventCache Version 1.0
	Copyright 2005 Mark Wubben
	Provides a way for automagically removing events from nodes and thus preventing memory leakage.
	See <http://novemberborn.net/javascript/event-cache> for more information.
	This software is licensed under the CC-GNU LGPL <http://creativecommons.org/licenses/LGPL/2.1/>
*/

//EVENT CACHE
var EventCache = function(){
	var listEvents = [];
	return {
		listEvents : listEvents,
		add : function(node, sEventName, fHandler){
			listEvents.push(arguments);
		},
		flush : function(){
			var i, item;
			for(i = listEvents.length - 1; i >= 0; i = i - 1){
				item = listEvents[i];
				if(item[0].removeEventListener){
					item[0].removeEventListener(item[1], item[2], item[3]);
				};
				if(item[1].substring(0, 2) != "on"){
					item[1] = "on" + item[1];
				};
				if(item[0].detachEvent){
					item[0].detachEvent(item[1], item[2]);
				};
				item[0][item[1]] = null;
			};
		}
	};
}();

addEvent(window,'unload',EventCache.flush);

// SNIFF -> SPRITE SWAP
function sniffSpriteSwap(fobj){
	fobj.onmouseout = function(){
		fobj.style.left = 0+'px';
	};
	fobj.onmouseover = function(){
		fobj.style.left = (fobj.width)/2 * -1 +'px';
	};
}


// SNIFF -> TICKER
function sniffTicker(fobj){
	fobj.omni = fobj.innerHTML;
	fobj.onclick = function(){
		s_linkType='o';
		s_linkName='ticker';
		s_prop15=s_pageName;
		s_prop16=this.omni;
		s_lnk=s_co(this);
		s_gs(s_account);
	};
}


// SNIFF -> AUTOCLEAR
function sniffAutoclear(fobj){
	fobj.onfocus = function(){
		if(this.value == this.defaultValue){
			this.value='';
		};
	};
	fobj.onblur = function(){
		if(this.value==''){
			this.value = this.defaultValue;
		}
	};
}


// CANCEL DEFAULT EVENT
function cancelDefault(e){
	if (is.ie567) {
		e.returnValue=false;
	}else{
		e.preventDefault();
	}
}


// SNIFF -> goto menu
function sniffGoto(fobj){
	if(hasClassName(fobj, 'showDiv')){
		addEvent(fobj,"change",function(){
				var divID = this.options[this.selectedIndex].value.split('#')[1];
				if (this.currentItem){
						addClassName(this.currentItem,'hidethis');
				}
				if(document.getElementById(divID)){
					this.currentItem = document.getElementById(divID);
					removeClassName(this.currentItem,'hidethis');
				}else{
					this.currentItem = null;
				}
		});
	}else{
		addEvent(fobj,"change",function(){
			if(this.options[this.selectedIndex].value != "" && this.options[this.selectedIndex].getAttribute("value")){
				document.location = this.options[this.selectedIndex].value;
			}
		});
	}
}

// SNIFF -> goto UL menu
function sniffGotoUL(fobj){
	var li = getChildNodesByTagName(fobj,'li');
	var options = "";
	var heading = prevElem(fobj);
	if(heading && hasClassName(heading, 'listTitle')){
		options = options+'<option value="">'+heading.innerHTML+'</option>\n<option value="">-------------------------------------------</option>';
	}
	var ulclass = "goto";
	var form = elem('form',{'action':''});
	fobj.parentNode.insertBefore(form, fobj);
	if(hasClassName(fobj, 'showDiv')){
		ulclass = "showDiv";
		var exdiv = document.createElement('div');
		fobj.parentNode.insertBefore(exdiv, fobj);
	}
	for (var n=0;n<li.length;n++){
		if (li[n].getElementsByTagName('a')[0]){
			options = options+'<option value="'+li[n].getElementsByTagName('a')[0].href+'">'+li[n].getElementsByTagName('a')[0].innerHTML+'</option>';
		}else if (li[n].innerHTML){
			options = options+'<option value="">'+li[n].innerHTML+'</option>';
		}
		if (hasClassName(fobj, 'showDiv') && li[n].getElementsByTagName('div')[0]){
			exdiv.appendChild(li[n].getElementsByTagName('div')[0]);
		}
	}
	form.innerHTML = '<select class="'+ulclass+'">'+options+'</select>';
	fobj.parentNode.removeChild(fobj);
}
