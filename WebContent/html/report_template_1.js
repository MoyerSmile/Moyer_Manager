window.SL_PageTemplate = {REPORT_PAGE:1,ADD_PAGE:2,page:{}};

SL_PageTemplate.setPageType = function(pageType){
	window.SL_PageTemplate.page.pageType = pageType;
};


SL_PageTemplate.init = function(){
	window.tinfo = {con:{},result:{},page:{},but:{}};//存入v-model 传输显示的值
	window.data = {page:{},condition:{},button:{},result:{namedata:{},desc:{}}};//信息值
}


window.$Text = function(html){
	var div = document.createElement("div");
	div.innerText = html;
	return div.innerHTML;
}

window.$SL = function(name){
	var obj = document.getElementById(name);
	obj.append = function(com){
		this.appendChild(com);
	};
	return obj;
};

SL_PageTemplate.setup = function(hasPageControl,url,number){
	window.data.page={hasPageControl:hasPageControl,url:url,number:number};
};

SL_PageTemplate.setReportCondition=function(data){
	window.data.condition={data};
}

SL_PageTemplate.setButton=function(butdata){
	window.data.button={butdata};
}

SL_PageTemplate.setHeader=function(namedata){
	window.data.result.namedata=namedata;
}

SL_PageTemplate.start = function(){
	
	SL_PageTemplate._renderLayout();
	SL_PageTemplate._renderHeader();
	SL_PageTemplate._renderFooter();
	SL_PageTemplate._renderMain();
	window.tinfo.but.submit();
	
	new Vue({
		el:"#app",
		data:window.tinfo
		});
	window.onresize();
}

SL_PageTemplate._renderLayout=function(){
	window.app = document.createElement("div");
	window.app.id = "app";
	if(window.data.page.hasPageControl==true){
		window.app.innerHTML = "<el-container id='_container'><el-header id='header' style='height:40px'></el-header><el-main id='main'></el-main><el-footer id='footer' style='height:10px'></el-footer></el-container>";
	}else{
		window.app.innerHTML = "<el-container><el-header id='header'></el-header><el-main id='main'></el-main></el-container>";
	}
	document.body.appendChild(window.app);
}

SL_PageTemplate._renderHeader=function(){
	var divWidth=0;
	var k=1;
	var htmlWidth=document.body.offsetWidth;
	if(window.data.condition.data!=null){
		
		window.tinfo.con.sub={};
		for(var i in window.data.condition.data){
			
			if(divWidth*2>htmlWidth){
				 k++;
				 divWidth=0;
				 var elm = document.getElementById('header');
				 elm.setAttribute("style","height:"+40*k+"px");
			 }//换行
			if(divWidth*2<htmlWidth){
				 k--;
				 divWidth=0;
				 var elm = document.getElementById('header');
				 elm.setAttribute("style","height:"+40*k+"px");
			 }//换行
			
			 window.WaiCen = document.createElement("div");
			 var waiCenId="waicen"+i;
			 window.WaiCen.id = waiCenId;
			 $SL("header").append(window.WaiCen);
			 var elm2 = document.getElementById(waiCenId);
			 elm2.setAttribute("style","float:left");
			 
			 var comData = window.data.condition.data[i];
			 var valueName=comData.name;
			 var descName=comData.desc; 
			 var opt=comData.desc+"option";
			 var widthL=comData.desc+"width";
			 window.Name = document.createElement("span");
			 window.Name.innerHTML=comData.label+":"+"&nbsp;&nbsp;";
			 $SL(waiCenId).append(window.Name);
			 
			 window.condition = document.createElement("span");
			 window.condition.id=valueName;
			 if(comData.com_type=="input"){
				 window.condition.innerHTML="<el-input v-model='con.sub."+valueName+"' size='mini' :placeholder='con.desc' :style='con."+widthL+"'></el-input>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				 $SL(waiCenId).append(window.condition);
				 divWidth=divWidth+comData.width;
			 }else if(comData.com_type=="select"){
				 window.condition.innerHTML="<el-select v-model='con.sub."+valueName+"' :placeholder='con.desc' size='mini' :style='con."+widthL+"'><el-option v-for='item in con."+opt+"' :key='item.value' :label='item.label' :value='item.value'></el-option> </el-select>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				 $SL(waiCenId).append(window.condition);
				 divWidth=divWidth+comData.width;
			 }else if(comData.com_type=="date"){
				 window.condition.innerHTML="<el-date-picker type='date' :placeholder='con.desc_"+valueName+"' size='mini' v-model='con.sub."+valueName+"' :style='con."+widthL+"' @change.native='con.getSTime' value-format='yyyy-MM-dd'></el-date-picker>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				 $SL(waiCenId).append(window.condition);
				 divWidth=divWidth+comData.width;
			 }else if(comData.com_type=="time"){
				 window.condition.innerHTML="<el-time-picker :placeholder='con.desc' size='mini' v-model='con.sub."+valueName+"' :style='con."+widthL+"' @change.native='con.getSTime' value-format='HH:mm:ss'></el-time-picker>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				 $SL(waiCenId).append(window.condition);
				 divWidth=divWidth+comData.width;
			 }else if(comData.com_type=="datetime"){
				 window.condition.innerHTML="<el-date-picker type='datetime' :placeholder='con.desc' size='mini' v-model='con.sub."+valueName+"' :style='con."+widthL+"' @change.native='con.getSTime' value-format='yyyy-MM-dd HH:mm:ss'></el-date-picker>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				 $SL(waiCenId).append(window.condition);
				 divWidth=divWidth+comData.width;
			 }
			 if(comData.com_type=="time"&&comData.com_type=="date"&&comData.com_type=="datetime"){
				 window.tinfo.con.getSTime = function(time) {
					 this.window.tinfo.con.sub[valueName]=time;
			     };
			 }
			 window.tinfo.con.sub[valueName]="";
			 window.tinfo.con["desc_"+valueName] = $Text(descName);
			 window.tinfo.con[opt] = comData.option;
			 window.tinfo.con[widthL] = "width:"+comData.width+"px";
		}
	}
	if(window.data.button.butdata!=null){
		for(var j in window.data.button.butdata){
			
			if(divWidth*2>htmlWidth){
				 k++;
				 divWidth=0;
				 var elm = document.getElementById('header');
				 elm.setAttribute("style","height:"+40*k+"px");
			 }//换行
			if(divWidth*2<htmlWidth){
				 k--;
				 divWidth=0;
				 var elm = document.getElementById('header');
				 elm.setAttribute("style","height:"+40*k+"px");
			 }//换行
			
			var buttonData=window.data.button.butdata[j];
			window.button = document.createElement("span");
			window.button.id =buttonData.name;
			if(buttonData.type=="submit"){
				window.button.innerHTML="<el-button size='mini' width="+buttonData.width+" type='primary' icon='el-icon-search' @click.native='but.submit' >"+buttonData.label+"</el-button>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				$SL(waiCenId).append(window.button);
				divWidth=buttonData.width;
				window.tinfo.but.submit=function(){
					var jsonObj={pageSize:window.tinfo.page.pageSize,curPage:window.tinfo.page.curPage};
					for(var i in window.tinfo.con.sub){
						var key = i
						if(window.tinfo.con.sub[i]==null&&window.tinfo.con.sub[i]==""){
							jsonObj[key]="";
						}else{
							jsonObj[key]=window.tinfo.con.sub[i];
						}
					}
						axios({
							    method:'post',//方法
							    url:window.data.page.url,//地址
							    data:jsonObj
						})
				        .then(function (response) {
				        	window.tinfo.page.total=response.data.total;
				        	window.tinfo.result.tableData = response.data.list;
				        })
				        .catch(function (error) {
				           window.SL_PageTemplate.innerHTML="<el-alert title='查询失败' type='error' show-icon center></el-alert>";
				           console.log(error);
				        });
						
					}
				
				}
			if(buttonData.type=="add"){
				window.button.innerHTML="<el-button size='mini' width="+buttonData.width+" type='primary' icon='el-icon-circle-plus' @click='but.formVisible=true'>"+buttonData.label+"</el-button>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				$SL(waiCenId).append(window.button);
				divWidth=buttonData.width;
					
					window.tinfo.but.resultData={};
					var winopt = buttonData.option
					
					window.winAdd = document.createElement("div");
					window.winAdd.id ="add";
					var wincolum=[];
					wincolum[wincolum.length]="<el-dialog title="+winopt.winN+" :visible.sync='but.formVisible' width='"+winopt.winW+"'><el-form :model='but.resultData'>";
					for(var a in winopt.winBut){
						var name=winopt.winBut[a].name;
						if(winopt.winBut[a].type=="input"){
							wincolum[wincolum.length]="<el-form-item label='"+winopt.winBut[a].label+"' label-width='120px'>";
							wincolum[wincolum.length]="<el-input :v-model='but.resultData."+name+"' auto-complete='off' style='width:"+winopt.winBut[a].width+"'></el-input></el-form-item>";
							window.tinfo.but.resultData[name]="";
						}
					}
					wincolum[wincolum.length]="</el-form><div slot='footer' class='dialog-footer'><el-button @click='but.formVisible = false'>取 消</el-button><el-button type='primary' @click='but.save'>保存</el-button></div></el-dialog>";
					window.winAdd.innerHTML=wincolum.join("");
					$SL("header").append(window.winAdd);
					window.tinfo.but.formVisible=false;
					
					window.tinfo.but.save=function(){
						var jsonSave={};
						for(var i in window.tinfo.but.resultData){
							var keySave = i
							if(window.tinfo.but.resultData[i]==null&&window.tinfo.but.resultData[i]==""){
								jsonSave[keySave]="";
							}else{
								jsonSave[keySave]=window.tinfo.but.resultData[i];
							}
							
							axios({
							    method:'post',//方法
							    url:winopt.winUrl,//地址
							    data:jsonSave
						})
				        .then(function (response) {
				        })
				        .catch(function (error) {
				           console.log(error);
				        });
						}
					}
			}
			if(buttonData.type=="export"){
				window.button.innerHTML="<el-button size='mini' width="+buttonData.width+" type='primary' icon='el-icon-download' @click.native='but.epot'>"+buttonData.label+"</el-button>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				$SL(waiCenId).append(window.button);
				divWidth=buttonData.width;
				window.tinfo.but.epot=function(){
//					for(var i in window.tinfo.con.sub){
//						var labelJ={};
//						var nameJ={};
//						for(var i  in window.data.result.namedata){
//							labelJ[i]=window.data.result.namedata[i].label;
//							nameJ[i]=window.data.result.namedata[i].prop;
//						}
//						 const { export_json_to_excel } = require('Export2Excel');
//				  　　　　　　const tHeader = labelJ;
//				  　　　　　　const filterVal = nameJ;
//				  　　　　　　const list = this.window.data.result.desc;
//				  　　　　　　const data = this.formatJson(filterVal, list);
//				  　　　　　　export_json_to_excel(tHeader, data, '表格');
//						
//					}
				};
//				window.tinfo.but.formatJson=function(filterVal, jsonData) {
//					　　return jsonData.map(v => filterVal.map(j => v[j]))
//				}
//				
			}
		}
	}
}

SL_PageTemplate._renderMain=function(){
	var nameD = window.data.result.namedata;
	var descD = window.data.result.desc;
	window.Main = document.createElement("div");
	window.Main.id ="maindata";
	var colum=[];
	colum[colum.length]="<el-table :data='result.tableData' stripe style='width: 100% ;'>";
	for(var i in nameD){ 
		if(nameD[i].width!=null&&nameD[i].ifsort==true){
			colum[colum.length]="<el-table-column align='center' prop='"+nameD[i].prop+"' label='"+nameD[i].label+"' width='"+nameD[i].width+"' v-if='"+nameD[i].ifshow+"'  sortable></el-table-column>";
			}else if(nameD[i].width==null&&nameD[i].ifsort==true){
				colum[colum.length]="<el-table-column align='center' prop='"+nameD[i].prop+"' label='"+nameD[i].label+"' v-if='"+nameD[i].ifshow+"'  sortable></el-table-column>";
			}else if(nameD[i].width!=null&&nameD[i].ifsort==false){
				colum[colum.length]="<el-table-column align='center' prop='"+nameD[i].prop+"' label='"+nameD[i].label+"' width='"+nameD[i].width+"' v-if='"+nameD[i].ifshow+"'></el-table-column>";
			}else{
				colum[colum.length]="<el-table-column align='center' prop='"+nameD[i].prop+"' label='"+nameD[i].label+"' v-if='"+nameD[i].ifshow+"' ></el-table-column>";
			}
		}
	colum[colum.length] = "</el-table>";
	window.Main.innerHTML=colum.join("");
	$SL("main").append(window.Main);
	if(descD.length>0){
		window.tinfo.result.tableData = descD;
	}
}

SL_PageTemplate._renderFooter=function(){
	if(window.data.page.hasPageControl==true){
		window.FenYe = document.createElement("div");
		window.FenYe.id = "page";
		window.FenYe.innerHTML ="<el-pagination @size-change='page.sizeChanged' @current-change='page.curPageChanged' :current-page='page.curPage' :page-sizes='page.pageSizeArr' :page-size='page.pageSize' layout='total, sizes, prev, pager, next, jumper' :total='page.total'></el-pagination>";
		$SL("footer").append(window.FenYe);
		
		window.tinfo.page.sizeChanged = function(size) {
			    	 this.window.tinfo.page.pageSize=size;
			    	 window.tinfo.but.submit();
			      };
		window.tinfo.page.curPageChanged = function(currentPage) {
			    	 this.window.tinfo.page.curPage=currentPage;
			    	 window.tinfo.but.submit();
			      };
		window.tinfo.page.curPage = 1;  
		window.tinfo.page.pageSizeArr = [10,20,50,100];
		window.tinfo.page.pageSize = window.data.page.number;
		window.tinfo.page.total = window.data.page.total;
	}
}

window.onresize = function(){
	document.getElementById("maindata").style.height = document.body.clientHeight - 200;
	document.getElementById("_container").style.height = document.body.clientHeight - 100;
}
