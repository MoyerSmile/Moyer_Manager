window.SL_PageTemplate = {REPORT_PAGE:1,ADD_PAGE:2,page:{}};

SL_PageTemplate.setPageType = function(pageType){
	window.SL_PageTemplate.page.pageType = pageType;
};


SL_PageTemplate.init = function(){
	window.tinfo = {con:{},result:{},page:{},but:{}};//存入v-model 传输显示的值
	window.data = {page:{},condition:{},button:{},result:{namedata:{},desc:{}}};//信息值
	window.temporary={};//临时数据
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

SL_PageTemplate.setup = function(hasPageControl,url,number,ifsub){
	window.data.page={hasPageControl:hasPageControl,url:url,number:number,ifsub:ifsub};
};

SL_PageTemplate.setReportCondition=function(data){
	window.data.condition={data};
}

SL_PageTemplate.setButton=function(butdata){
	window.data.button={butdata};
}

SL_PageTemplate.setHeader=function(namedata,url){
	window.data.result.namedata=namedata;
	window.data.result.menuurl=url;
}

SL_PageTemplate.start = function(){
	
	SL_PageTemplate._renderLayout();
	SL_PageTemplate._renderHeader();
	SL_PageTemplate._renderMain();
	SL_PageTemplate._renderFooter();
	for(var j in window.data.button.butdata){
		var buttonData=window.data.button.butdata[j];
		if(buttonData.type=="submit"){
			if(window.data.page.ifsub==true){
				window.tinfo.but.submit();
			}
		}
	}
	window.onresize();
	new Vue({
		el:"#app",
		data:window.tinfo
		});
	
	 var nameD = window.data.result.namedata;
	 document.getElementById("MenuList").addEventListener("mouseleave",function(){
		 var MenuList = document.getElementById("MenuList");
		 if(MenuList.style.display==""){
				MenuList.style.display="none";
				for(var i in nameD){
					 for(var j in window.tinfo.result.checkList){
						 if(nameD[i].label==window.tinfo.result.checkList[j]){
							 window.tinfo.result["ifshow_"+nameD[i].prop] = true;
							 break;
						 }else{
							 window.tinfo.result["ifshow_"+nameD[i].prop] = false;
						 }
					 }
				 }
				 uploadMenu();
		 }
	 });
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
			
			 window.Out = document.createElement("div");
			 var OutId="out"+i;
			 window.Out.id = OutId;
			 $SL("header").append(window.Out);
			 var elm2 = document.getElementById(OutId);
			 elm2.setAttribute("style","float:left");
			 
			 var comData = window.data.condition.data[i];
			 var valueName=comData.name;
			 var opt=comData.desc+"option";
			 window.Name = document.createElement("span");
			 window.Name.innerHTML=comData.label+":"+"&nbsp;&nbsp;";
			 $SL(OutId).append(window.Name);
			 
			 window.condition = document.createElement("span");
			 window.condition.id=valueName;
			 if(comData.com_type=="input"){
				 window.condition.innerHTML="<el-input v-model='con.sub."+valueName+"' size='mini' :placeholder='con.desc_"+valueName+"' style='width:"+comData.width+"'></el-input>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				 $SL(OutId).append(window.condition);
				 divWidth=divWidth+comData.width;
			 }else if(comData.com_type=="select"){
				 window.condition.innerHTML="<el-select v-model='con.sub."+valueName+"' :placeholder='con.desc_"+valueName+"' size='mini' style='width:"+comData.width+"'><el-option v-for='item in con."+opt+"' :key='item.value' :label='item.label' :value='item.value'></el-option> </el-select>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				 $SL(OutId).append(window.condition);
				 divWidth=divWidth+comData.width;
			 }else if(comData.com_type=="date"){
				 window.condition.innerHTML="<el-date-picker type='date' :placeholder='con.desc_"+valueName+"' size='mini' v-model='con.sub."+valueName+"' style='width:"+comData.width+"' @change.native='con.getSTime' value-format='yyyy-MM-dd'></el-date-picker>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				 $SL(OutId).append(window.condition);
				 divWidth=divWidth+comData.width;
			 }else if(comData.com_type=="time"){
				 window.condition.innerHTML="<el-time-picker :placeholder='con.desc_"+valueName+"' size='mini' v-model='con.sub."+valueName+"' style='width:"+comData.width+"' @change.native='con.getSTime' value-format='HH:mm:ss'></el-time-picker>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				 $SL(OutId).append(window.condition);
				 divWidth=divWidth+comData.width;
			 }else if(comData.com_type=="datetime"){
				 window.condition.innerHTML="<el-date-picker type='datetime' :placeholder='con.desc_"+valueName+"' size='mini' v-model='con.sub."+valueName+"' style='width:"+comData.width+"' @change.native='con.getSTime' value-format='yyyy-MM-dd HH:mm:ss'></el-date-picker>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				 $SL(OutId).append(window.condition);
				 divWidth=divWidth+comData.width;
			 }
			 if(comData.com_type=="time"&&comData.com_type=="date"&&comData.com_type=="datetime"){
				 window.tinfo.con.getSTime = function(time) {
					 this.window.tinfo.con.sub[valueName]=time;
			     };
			 }
			 window.tinfo.con.sub[valueName]="";
			 window.tinfo.con["desc_"+valueName] = $Text(comData.desc);
			 window.tinfo.con[opt] = comData.option;
		}
	}
	if(window.data.button.butdata!=null){
		for(var j in window.data.button.butdata){
			
			var buttonData=window.data.button.butdata[j];
			window.button = document.createElement("span");
			window.button.id =buttonData.name;
			if(buttonData.type=="submit"){
				window.button.innerHTML="<el-button size='mini' width="+buttonData.width+" type='primary' icon='el-icon-search' @click.native='but.submit' >"+buttonData.label+"</el-button>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				$SL(OutId).append(window.button);
				divWidth=buttonData.width;
				var ifsub =window.data.page.ifsub;
				window.tinfo.but.submit=function(){
					let loading = Vue.prototype.$loading({text:"加载中"});
					var jsonObj={};
					for(var i in window.tinfo.con.sub){
						var key = i
						if(window.tinfo.con.sub[i]==null&&window.tinfo.con.sub[i]==""){
							jsonObj[key]="";
						}else{
							jsonObj[key]=window.tinfo.con.sub[i];
						}
					}
					window.temporary=jsonObj;
						axios({
							    method:'post',//方法
							    url:window.data.page.url,//地址
							    data:{
							    	sub : jsonObj,
							    	pageSize:window.tinfo.page.pageSize,
							    	currentPage:1
							    }
						}).then(function (response) {
				        	window.tinfo.page.total=response.data.total;
				        	window.tinfo.result.tableData = response.data.list;
				        	window.tinfo.page.curPage = 1;  
				        	loading.close();
				        }).catch(function (error) {
				           console.log(error);
				        });
						
					}
				
				}
			if(buttonData.type=="add"){
				window.button.innerHTML="<el-button size='mini' width="+buttonData.width+" type='primary' icon='el-icon-circle-plus' @click='but.formVisible=true'>"+buttonData.label+"</el-button>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				$SL(OutId).append(window.button);
				divWidth=buttonData.width;
					
					window.tinfo.but.resultData={};
					var winopt = buttonData.option
					
					window.winAdd = document.createElement("div");
					window.winAdd.id ="add";
					var wincolum=[];
					wincolum[wincolum.length]="<el-dialog title="+winopt.winN+" :visible.sync='but.formVisible' width='"+winopt.winW+"' @close='but.close'><el-form :model='but.resultData'>";
					for(var a in winopt.winBut){
						var name=winopt.winBut[a].name;
						if(winopt.winBut[a].type=="input"&&winopt.winBut[a].ifadd==true){
							if(winopt.winBut[a].desc=="密码"||winopt.winBut[a].desc=="password"){
								wincolum[wincolum.length]="<el-form-item label='"+winopt.winBut[a].label+"' label-width='120px'>";
								wincolum[wincolum.length]="<el-input size='small' v-model='but.resultData."+name+"' auto-complete='off' style='width:"+winopt.winBut[a].width+"' type='password'></el-input></el-form-item>";
								window.tinfo.but.resultData[name]="";
							}else{
							wincolum[wincolum.length]="<el-form-item label='"+winopt.winBut[a].label+"' label-width='120px'>";
							wincolum[wincolum.length]="<el-input size='small' v-model='but.resultData."+name+"' auto-complete='off' style='width:"+winopt.winBut[a].width+"'></el-input></el-form-item>";
							window.tinfo.but.resultData[name]="";
							}
						}else if(winopt.winBut[a].type=="date"&&winopt.winBut[a].ifadd==true){
							 wincolum[wincolum.length]="<el-form-item label='"+winopt.winBut[a].label+"' label-width='120px'>";
							 wincolum[wincolum.length]="<el-date-picker size='small' type='date' auto-complete='off' v-model='but.resultData."+name+"' style='width:"+winopt.winBut[a].width+"' @change.native='con.getSTime' value-format='yyyy-MM-dd'></el-date-picker></el-form-item>";
							 window.tinfo.but.resultData[name]="";
						}else if(winopt.winBut[a].type=="time"&&winopt.winBut[a].ifadd==true){
							 wincolum[wincolum.length]="<el-form-item label='"+winopt.winBut[a].label+"' label-width='120px'>";
							 wincolum[wincolum.length]="<el-time-picker size='small' auto-complete='off' v-model='but.resultData."+name+"' style='width:"+winopt.winBut[a].width+"' @change.native='con.getSTime' value-format='HH:mm:ss'></el-time-picker></el-form-item>";
							 window.tinfo.but.resultData[name]="";
						}else if(winopt.winBut[a].type=="datetime"&&winopt.winBut[a].ifadd==true){
							 wincolum[wincolum.length]="<el-form-item label='"+winopt.winBut[a].label+"' label-width='120px'>";
							 wincolum[wincolum.length]="<el-date-picker size='small' type='datetime' auto-complete='off' v-model='but.resultData."+name+"' style='width:"+winopt.winBut[a].width+"' @change.native='but.getSTime' value-format='yyyy-MM-dd HH:mm:ss'></el-date-picker></el-form-item>";
							 window.tinfo.but.resultData[name]="";
						}else if(winopt.winBut[a].type=="select"&&winopt.winBut[a].ifadd==true){
							 var opt=winopt.winBut[a].name+"option";
							 wincolum[wincolum.length]="<el-form-item label='"+winopt.winBut[a].label+"' label-width='120px'>";
							 wincolum[wincolum.length]="<el-select v-model='but.resultData."+name+"'  auto-complete='off' size='small' style='width:"+winopt.winBut[a].width+"'><el-option v-for='item in but."+opt+"' :key='item.value' :label='item.label' :value='item.value'></el-option> </el-select></el-form-item>";
							 window.tinfo.but.resultData[name]="";
							 window.tinfo.but[opt] = winopt.winBut[a].option;
						}else if(winopt.winBut[a].type=="checkbox"&&winopt.winBut[a].ifadd==true){
							 wincolum[wincolum.length]="<el-form-item label='"+winopt.winBut[a].label+"' label-width='120px'>";
							 wincolum[wincolum.length]="<el-checkbox-group v-model='but.resultData."+name+"' name='"+name+"'>";
							 for(var k in winopt.winBut[a].option){
								 wincolum[wincolum.length]="<el-checkbox auto-complete='off' label='"+winopt.winBut[a].option[k].label+"'></el-checkbox>"
							 }
							 wincolum[wincolum.length]="</el-checkbox-group></el-form-item>";
							 window.tinfo.but.resultData[name]=[];
						}else if(winopt.winBut[a].type=="switch"&&winopt.winBut[a].ifadd==true){
							 wincolum[wincolum.length]="<el-form-item label='"+winopt.winBut[a].label+"' label-width='120px'>";
							 wincolum[wincolum.length]="<el-switch v-model='but.resultData."+name+"'></el-switch></el-form-item>";
							 window.tinfo.but.resultData[name]="";
						}
						 if(winopt.winBut[a].type=="time"&&winopt.winBut[a].type=="date"&&winopt.winBut[a].type=="datetime"){
							 window.tinfo.but.getSTime = function(time) {
								 this.window.tinfo.con.sub[valueName]=time;
						     };
						 }
					}
					
					wincolum[wincolum.length]="</el-form><div slot='footer' class='dialog-footer'><el-button @click='but.close'>取 消</el-button><el-button type='primary' @click='but.save'>保存</el-button></div></el-dialog>";
					window.winAdd.innerHTML=wincolum.join("");
					$SL("header").append(window.winAdd);
					window.tinfo.but.formVisible=false;
					window.tinfo.but.close=function(){
						for(var a in winopt.winBut){
							var name=winopt.winBut[a].name;
							window.tinfo.but.resultData[name]="";
						}
						window.tinfo.but.formVisible=false;
					}
					window.tinfo.but.save=function(){
						
						var jsonSave={};
						for(var i in window.tinfo.but.resultData){
							var keySave = i
							if(window.tinfo.but.resultData[i]==null&&window.tinfo.but.resultData[i]==""){
								jsonSave[keySave]="";
							}else{
								jsonSave[keySave]=window.tinfo.but.resultData[i];
							}
						}
						
						axios({
						    method:'post',//方法
						    url:winopt.winUrl,//地址
						    data:jsonSave
						}).then(function (response) {
								window.tinfo.but.close();
								window.tinfo.but.submitPage();
						}).catch(function (error) {
							console.log(error);
			        });
					}
				}
			if(buttonData.type=="reset"){
				window.button.innerHTML="<el-button size='mini' width="+buttonData.width+" type='primary' icon='el-icon-refresh' @click.native='but.reset'>"+buttonData.label+"</el-button>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				$SL(OutId).append(window.button);
				divWidth=buttonData.width;
				window.tinfo.but.reset=function(){
					for(var i in window.data.condition.data){
						window.tinfo.con.sub[window.data.condition.data[i].name]="";
						window.temporary[window.data.condition.data[j].name]="";
					}
				};
			}
			if(buttonData.type=="else"){
				window.button.innerHTML="<el-button size='mini' width="+buttonData.width+" type='primary'  @click.native='but.else_"+buttonData.name+"'>"+buttonData.label+"</el-button>"+"&nbsp;&nbsp;&nbsp;&nbsp;";
				$SL(OutId).append(window.button);
				divWidth=buttonData.width;
				window.tinfo.but["else_"+buttonData.name]=buttonData.option;
			}
		}
	}
}

SL_PageTemplate._renderMain=function(){
	var nameD = window.data.result.namedata;
	window.Main = document.createElement("div");
	window.Main.id ="maindata";
	var colum=[];
	colum[colum.length]="<el-table :data='result.tableData' :height='result.height' stripe style='width: 100% ;' @sort-change='result.changeSort' @row-dblclick='result.dbclick'><el-table-column type='index' label='序号' width='100' align='center' ></el-table-column>";
	colum[colum.length]="<el-table-column prop='id' label='id' v-if='false'></el-table-column>"
	for(var i in nameD){ 
		if(nameD[i].width!=null&&nameD[i].ifsort==true){
			colum[colum.length]="<el-table-column align='"+nameD[i].where+"' prop='"+nameD[i].prop+"' label='"+nameD[i].label+"' width='"+nameD[i].width+"' v-if='result.ifshow_"+nameD[i].prop+"'  sortable='custom' ></el-table-column>";
			}else if(nameD[i].width==null&&nameD[i].ifsort==true){
				colum[colum.length]="<el-table-column align='"+nameD[i].where+"' prop='"+nameD[i].prop+"' label='"+nameD[i].label+"' v-if='result.ifshow_"+nameD[i].prop+"'  sortable='custom'></el-table-column>";
			}else if(nameD[i].width!=null&&nameD[i].ifsort==false){
				colum[colum.length]="<el-table-column align='"+nameD[i].where+"' prop='"+nameD[i].prop+"' label='"+nameD[i].label+"' width='"+nameD[i].width+"' v-if='result.ifshow_"+nameD[i].prop+"'></el-table-column>";
			}else{
				colum[colum.length]="<el-table-column align='"+nameD[i].where+"' prop='"+nameD[i].prop+"' label='"+nameD[i].label+"' v-if='result.ifshow_"+nameD[i].prop+"' ></el-table-column>";
			}
		}
	colum[colum.length] = "</el-table>";
	window.Main.innerHTML=colum.join("");
	$SL("main").append(window.Main);
	window.tinfo.result.tableData = "";
	
	window.tinfo.result.dbclick=function(row){
		for(var j in window.data.button.butdata){
			var buttonData=window.data.button.butdata[j];
			if(buttonData.type=="update"){
				window.tinfo.but.resultUpdate={};
				var winopt = buttonData.option
				var urlUpadte= winopt.winUrl;
				window.winUpdate = document.createElement("div");
				window.winUpdate.id ="update";
				var wincolumupdate=[];
				wincolumupdate[wincolumupdate.length]="<el-dialog title="+winopt.winN+" :visible.sync='but.formVisibleUpdate' width='"+winopt.winW+"' @close='but.closeUpdate'><el-form :model='but.resultData'>";
				for(var a in winopt.winBut){
					var name=winopt.winBut[a].name;
					var desc=winopt.winBut[a].desc
					if(winopt.winBut[a].type=="input"&&winopt.winBut[a].ifupdate==true){
						wincolumupdate[wincolumupdate.length]="<el-form-item label='"+winopt.winBut[a].label+"' label-width='120px'>";
						wincolumupdate[wincolumupdate.length]="<el-input v-model='but.resultUpdate."+name+"' auto-complete='off' style='width:"+winopt.winBut[a].width+"'></el-input></el-form-item>";
						window.tinfo.but.resultUpdate[name]="";
					}
				}
				wincolumupdate[wincolumupdate.length]="</el-form><div slot='footer' class='dialog-footer'><el-button @click='but.closeUpdate'>取 消</el-button><el-button type='primary' @click='but.saveUpdate'>保存</el-button></div></el-dialog>";
				window.winUpdate.innerHTML=wincolumupdate.join("");
				$SL("header").append(window.winUpdate);
				window.tinfo.but.formVisibleUpdate=true;
				
				window.tinfo.but.closeUpdate=function(){
					for(var a in winopt.winBut){
						var name=winopt.winBut[a].name;
						window.tinfo.but.resultUpdate[name]="";
					}
					window.tinfo.but.formVisibleUpdate=false;
				}
			}
		}
		alert(row.id);
	}
	
	for(var i in nameD){ 
		window.tinfo.result["ifshow_"+nameD[i].prop] = nameD[i].ifshow;
	}
	window.tinfo.result.changeSort=function(column){
		let loading = Vue.prototype.$loading({text:"加载中"});
		axios({
		    method:'post',//方法
		    url:window.data.page.url,//地址
		    data:{
		    	prop : column.prop,
		    	order : column.order,
		    	sub : window.temporary,
		    	pageSize :window.tinfo.page.pageSize
		    }
	}).then(function (response) {
    	window.tinfo.page.total=response.data.total;
    	window.tinfo.result.tableData = response.data.list;
    	window.tinfo.page.curPage = 1;  
    	loading.close();
    }).catch(function (error) {
       console.log(error);
    });
	}
	
	 var nameD = window.data.result.namedata;
	 var elmmenuM = document.getElementById("maindata");
	 elmmenuM.setAttribute("style"," position:relative;" );
	 
	 uploadMenu=function(){
		 var resultData={};
		 var nameD=window.data.result.namedata;
		 for(var i in nameD){
			 resultData[nameD[i].prop]=window.tinfo.result["ifshow_"+nameD[i].prop];
		 }
			axios({
			    method:'post',//方法
			    url:window.data.result.menuurl,//地址
			    data:resultData
		}).then(function (response) {
        }) .catch(function (error) {
           console.log(error);
        });
	 }
	var button=[];
	window.button=document.createElement("div");
	window.button.id="button";
	button[button.length]=" <el-button icon='el-icon-tickets' type='text' @click.native='but.hidden'></el-button>";
	window.tinfo.but.hidden=function(){
		var MenuList = document.getElementById("MenuList");
		if(MenuList.style.display==""){
			MenuList.style.display="none";
			for(var i in nameD){
				 for(var j in window.tinfo.result.checkList){
					 if(nameD[i].label==window.tinfo.result.checkList[j]){
						 window.tinfo.result["ifshow_"+nameD[i].prop] = true;
						 break;
					 }else{
						 window.tinfo.result["ifshow_"+nameD[i].prop] = false;
					 }
				 }
			 }
			 uploadMenu();
		}else{
			MenuList.style.display="";
		}
	}
	
	window.button.innerHTML=button.join("");
	$SL("maindata").append(window.button);
	
	 var columMenuList=[];
	 window.MenuList = document.createElement("div");
	 
	 window.MenuList.id="MenuList";
	 columMenuList[columMenuList.length]="<el-checkbox-group v-model='result.checkList'>";
	 for(var i in nameD){
		 columMenuList[columMenuList.length]="<el-checkbox label='"+nameD[i].label+"'></el-checkbox><br/>";
	 }
	 columMenuList[columMenuList.length]="</el-checkbox-group>";
	 
	 var resultList=[];
	 for(var i in nameD){
		 if(nameD[i].ifshow==true){
			 resultList.push(nameD[i].label);
		 }
	 }
	 
	 window.tinfo.result.checkList=resultList;
	 window.MenuList.innerHTML=columMenuList.join("");
	 $SL("button").append(window.MenuList);
	 var button = document.getElementById("button");
	 button.setAttribute("style","position: absolute; z-index:999999;left:0; top:1.4%;" );
	 var MenuList = document.getElementById("MenuList");
	 MenuList.setAttribute("style","position: absolute; z-index:999999;background-color:#FFFFFF;top:40px;display:none;" );
	  
}

SL_PageTemplate._renderFooter=function(){
	if(window.data.page.hasPageControl==true){
		window.FenYe = document.createElement("div");
		window.FenYe.id = "page";
		window.FenYe.innerHTML ="<el-pagination @size-change='page.sizeChanged' @current-change='page.curPageChanged' :current-page='page.curPage' :page-sizes='page.pageSizeArr' :page-size='page.pageSize' layout='total, sizes, prev, pager, next' :total='page.total' ></el-pagination>";
		$SL("footer").append(window.FenYe);
		
		window.tinfo.page.sizeChanged = function(size) {
			    	 this.window.tinfo.page.pageSize=size;
			    	 window.temporary.pageSize=size;
			    	 window.tinfo.page.curPage=1;
			    	 window.tinfo.but.submitPage();
			      };
		window.tinfo.page.curPageChanged = function(currentPage) {
			    	 this.window.tinfo.page.curPage=currentPage;
			    	 window.tinfo.but.submitPage();
			      };
		window.tinfo.page.curPage = 1;  
		window.tinfo.page.pageSizeArr = [10,20,50,100];
		window.tinfo.page.pageSize = window.data.page.number;
		window.tinfo.page.total =0;
	}
	window.tinfo.but.submitPage=function(){
		let loading = Vue.prototype.$loading({text:"加载中"});
		axios({
			    method:'post',//方法
			    url:window.data.page.url,//地址
			    data:{
			    	sub : window.temporary,
			    	currentPage : window.tinfo.page.curPage,
			    	pageSize :  window.tinfo.page.pageSize
			    }
		}).then(function (response) {
        	window.tinfo.page.total=response.data.total;
        	window.tinfo.result.tableData = response.data.list;
        	loading.close();
        }).catch(function (error) {
           console.log(error);
        });
	}
}

window.onresize = function(){
	document.getElementById("maindata").style.height = document.body.clientHeight - 200;
	document.getElementById("_container").style.height = document.body.clientHeight - 70;
	window.tinfo.result.height = document.getElementById("maindata").offsetHeight ;
}
