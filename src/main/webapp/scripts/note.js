//单击笔记本加载笔记列表
function loadnotes(){
					//给笔记本li设置选中样式
					$("#book_list li a").removeClass("checked");
					$(this).find("a").addClass("checked");
					
					//获取bookId（点哪个获取哪个用this，加$(this)表示转成JQuery对象）
					var bookId = $(this).data("bookId");
					
					//发送请求
					$.ajax({
						url:"http://localhost:8080/cloud_note/note/loadnotes.do",
						type:"post",
						data:{"bookId":bookId},
						dataType:"json",
						success:function(result){
							//每次刷新笔记列表
							$("#note_list").empty();	//方法1
							//$("#note_list li").remove();	//方法2
							
							if(result.status==0){
								var notes = result.data;	//获取返回的笔记集合
								for(var i=0;i<notes.length;i++){
									var noteId = notes[i].cn_note_id;//获取笔记ID
									var noteTitle = notes[i].cn_note_title;//获取笔记标题
									//拼成一个笔记列表的li
									var s_li = 	'<li class="online">';
									s_li +=			'<a>';
									s_li +=				'<i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i> '+noteTitle+'<button type="button" class="btn btn-default btn-xs btn_position btn_slide_down"><i class="fa fa-chevron-down"></i></button>';
									s_li +=			'</a>';
									s_li +=			'<div class="note_menu" tabindex="-1">';
									s_li +=				'<dl>';
									s_li +=					'<dt><button type="button" class="btn btn-default btn-xs btn_move" title="移动至..."><i class="fa fa-random"></i></button></dt>';
									s_li +=					'<dt><button type="button" class="btn btn-default btn-xs btn_share" title="分享"><i class="fa fa-sitemap"></i></button></dt>';
									s_li +=					'<dt><button type="button" class="btn btn-default btn-xs btn_delete" title="删除"><i class="fa fa-times"></i></button></dt>';
									s_li +=				'</dl>';
									s_li +=			'</div>';
									s_li += 	'</li>';

									var $li = $(s_li);
									$li.data("noteId",noteId);//给li绑定笔记ID

									//将笔记li添加到笔记ul中
									$("#note_list").append($li);
									
								}
							}
						}
					});
					
					
					
					
				}