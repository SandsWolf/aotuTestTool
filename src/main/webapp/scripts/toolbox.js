//加载工具箱列表
function loadToolboxList(ip){
	$.ajax({
		url:ip+"/toolbox/loadlist",
		type:"post",
		data:{},
		dataType:"json",
		success:function(result){
			if(result.status == 0){
				var boxList = result.data;
				
				for(var i=0;i<boxList.length;i++){
					var boxName = boxList[i].toolbox_name;
					var toolboxId = boxList[i].toolbox_id;
					var status = boxList[i].status;
					
					var s_li = '<li class="online"><a>';
					s_li += '<i class="fa fa-book" title="online" rel="tooltip-bottom">';
					s_li += '</i><b>'+boxName+'</b></a></li>';
					
					var $li = $(s_li);
					$li.data("toolboxId",toolboxId);
					
					$("#toolbox_list").append($li);
				}
			}
		},
		error:function(){
			alert("error");
		}
	});
}