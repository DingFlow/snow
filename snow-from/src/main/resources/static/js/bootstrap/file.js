define(["require","assist","createDom","global"],function(require,Assist,CreateDom,Global){
    /**
     * 监听文件变化，并且实时上传
     */
    let changeLabelName = function(e){
        let filelists = $(this).prop('files');
        let names = [];
        for(let file of filelists){
            names.push(file.name);
        };
        names.length && $(this).next("label").text(names);
    }

    let component_properties = {
        data:{
            id:"",//id
            name:'',//name
            // defaultVal:"",//默认值
            verification:"",//校验
            authority:"edit",//权限
            // placeholder:"请输入",
            cssClass:"custom-file-input",//css类
            // hideLabel:false,//是否隐藏标签
            labelName:"名称",//标签名称
            // labelPosition:"rowLeft",//标签位置
            // labelWidth:"",//标签宽度
            // labelCSS:"col-form-label col-2",//标签css类

            accept:"*/*",//接收类型
            browse:"上传",//浏览
            multiple:false,//多个文件
            onChangeName:changeLabelName,//文件改变，修改labelName
            onChangeUpload:'',//自动上传文件的函数，默认绑定uploadFile
            // uploadUrl:"http://localhost:3000/uploadfile",//自动上传的url
            uploadUrl:"/catarc_infoSys/api/sys/file/uploadMultipartFiles",//自动上传的url
            formName:"files",//form的name(上传时候的name)
            automatic:true,//自动上传
            title:"名称",
            ortumDelFile:function(e){$(this).parents('.input-group-append').eq(0).hide()},//删除
            ortumPreviewFile:"",//预览
            ortumDownFile:"",//下载

            onChange:"",//change事件
            onCallBack:'',//上传后回调
            onSuccess:'',//上传成功
            onError:'',//上传失败
            onBefore:"",
            onAfter:"",
            onLabelClick:"",//label点击事件
            previewName:"预览",//预览按钮的名称
            downName:"下载",//预览按钮的名称
            delName:"删除",//预览按钮的名称

            showFileName:true,
            showPrevBtn:true,
            showDownBtn:true,
            showDelBtn:true,
            labelBindInput:true,//label绑定for事件


            uuid:"",
            attributesArr:[],//属性数组
        },
        inputChange:["id","name","accept","verification","cssClass","labelName","formName","uploadUrl","title","previewName","downName","delName"],//input事件修改值
        clickChange:["authority","multiple","automatic","showFileName","showPrevBtn","showDownBtn","showDelBtn","labelBindInput"],
        purview:{//属性编辑权限
            id:3,//id
            name:3,
            // defaultVal:3,
            verification:3,
            authority:3,//权限
            // placeholder:3,
            cssClass:3,//css类
            // hideLabel:3,//是否隐藏标签
            labelName:3,//标签名称
            // labelPosition:3,//标签位置
            // labelWidth:1,//标签宽度
            // labelCSS:3,//标签css类

            accept:3,
            browse:3,//浏览
            multiple:3,
            uploadUrl:3,//自动上传的url
            formName:3,//form的name
            automatic:3,//自动上传
            title:3,

            showFileName:3,
            showPrevBtn:3,
            showDownBtn:3,
            showDelBtn:3,
            labelBindInput:3,

            previewName:3,//预览按钮的名称
            downName:3,//预览按钮的名称
            delName:3,//预览按钮的名称

        },
        dataShowType:{
            multiple:'switch',
            automatic:"switch",
            authority:"checkbox",
            showFileName:'switch',
            showPrevBtn:'switch',
            showDownBtn:'switch',
            showDelBtn:'switch',
            labelBindInput:'switch',
        },
        verify:{//编辑属性时的验证
            automatic:{
                click:function(globalComponent,e,val,checked){
                    /*获取要编辑的组件的属性*/
                    let ortum_component_properties = $(globalComponent).prop('ortum_component_properties');
                    /*修改前的值*/
                    let oldData = ortum_component_properties.data;

                    if(checked && !oldData.formName){
                        $(e.target).addClass('is-invalid');;
                        $(e.target).parent().find('.invalid-feedback').eq(0).text('自动上传，formName的值不能为空');
                        $(e.target).prop('checked',false);
                        return true;
                    }
                    if(checked && !oldData.uploadUrl){
                        $(e.target).addClass('is-invalid');
                        $(e.target).parent().find('.invalid-feedback').eq(0).text('自动上传，uploadUrl的值不能为空');
                        $(e.target).prop('checked',false);
                        return true;
                    }
                    $(e.target).removeClass('is-invalid')
                },
            },
        }
    }
    /**
     * 监听文件变化，并且实时上传
     */
    let uploadFile = function(e){
        let formName = $(this).attr("data-formname");
        let uploadUrl = $(this).attr("data-uploadurl");
        let itemParents = $(this).parents(".ortum_item").eq(0);
        let filelists = $(this).prop('files');
        let ctx=this;
        let name = $(this).attr("name");

        let uuid = $(this).parents(".ortum_item").eq(0).attr("ortum_uuid");
        let oldUUID = uuid;
        uuid && (uuid = uuid.replace(/-/g,''));
        let callBack = 'ortum_bootstrap_file_onCallBack_' + uuid;
        let onSuccess = 'ortum_bootstrap_file_onSuccess_' + uuid;
        let onError = 'ortum_bootstrap_file_onError_' + uuid;


        /*发送到后端*/
        let fd = new FormData();
        let L = filelists.length;
        /*为空不上传*/
        if(!L)return;

        for(let i=0;i<L;i++){
            fd.append(formName, filelists[i]);
        }
        $(itemParents).find(".progress").eq(0).css("display","flex");
        ortumReq({
            "url":uploadUrl,
            "method":"POST",
            "data":fd,
            "success":(xhr,e)=>{
                /*require("assist").infoTip("上传成功！");*/
                /*alert("上传成功！");*/
                $(ctx).parents(".ortum_item").eq(0).find(".input-group-append").eq(0).show();
                uuid && Function("if(typeof "+ onSuccess +" == 'function')return "+ onSuccess + "("+ xhr.responseText +",'"+ name +"')")();
            },
            "error":(xhr,e)=>{
                /*require("assist").dangerTip("上传失败！");*/
                /*alert("上传失败！");*/
                uuid && Function("if(typeof "+ onError +" == 'function')return "+ onError + "("+ xhr.responseText +",'"+ name +"')")();
            },
            progress:(xhr,e)=>{
                let pro = e.loaded/e.total * 100;
                $(itemParents).find(".progress-bar").eq(0).css("width", pro+"%");
                $(itemParents).find(".progress-bar").eq(0).attr("aria-valuenow", pro);
            },
            final:(xhr,e)=>{
                setTimeout(function(){
                    $(itemParents).find(".progress").eq(0).css("display","none");
                    $(itemParents).find(".progress-bar").eq(0).css("width","0%");
                    $(itemParents).find(".progress-bar").eq(0).attr("aria-valuenow", 0);
                },200);
                uuid && Function("if(typeof "+ callBack +" == 'function')return "+ callBack + "("+ xhr.responseText +",'"+ name +"')")();
            }
        });
    }

    /**
     * 功能：创建bootstrap的file
     * @param {*} parentDom 
     * @param {*} moreProps 一个json对象，
     * @param {*} moreProps.customProps 自定义属性
     * @param {*} moreProps.generateDom 函数转化为String，保存到script标签中
     * @param {*} moreProps.createJson 生成对应的json
     * @param {*} moreProps.HasProperties 保存组件的component_properties
     * @param {*} moreProps.clickChangeAttrs 是否允许修改点击属性（=== false的时候，去除点击修改属性）
     * @param {*} moreProps.dropAddComponent 拖拽添加组件
     * @param {*} moreProps.customName 自定义name
     * @param {*} moreProps.ortumChildren 插入<ortum_children>的data-order
     * @param {*} moreProps.nameSuffix 名称后缀
     */
    let FileDom = function(parentDom,moreProps=null){
        let customProps = null;
        // let generateDom =  null;
        let clickChangeAttrs = true;
        let dropAddComponent = true;
        let customName = '';//自定义name

        let createJson = false;
        let HasProperties = false;
        let ortumChildren = null;
        let nameSuffix = null;

        if(Assist.getDetailType(moreProps) == "Object"){
            customProps = (Assist.getDetailType(moreProps.customProps) == "Object" ? moreProps.customProps : null);
            // moreProps.generateDom !== null && moreProps.generateDom !== undefined && (generateDom =moreProps.generateDom);
            moreProps.createJson !== null && moreProps.createJson !== undefined && (createJson =moreProps.createJson);
            moreProps.HasProperties !== null && moreProps.HasProperties !== undefined && (HasProperties =moreProps.HasProperties);
            moreProps.customName !== null && moreProps.customName !== undefined && (customName =moreProps.customName);
            moreProps.clickChangeAttrs === false && (clickChangeAttrs = moreProps.clickChangeAttrs);
            moreProps.dropAddComponent === false && (dropAddComponent = moreProps.dropAddComponent);
            moreProps.ortumChildren !== null && moreProps.ortumChildren !== undefined && (ortumChildren = moreProps.ortumChildren);
            moreProps.nameSuffix !== null && moreProps.nameSuffix !== undefined && (nameSuffix = moreProps.nameSuffix);
        }

        let outerDom=$(
            `
            <div class="form-group ortum_item ortum_bootstrap_file" data-frame="Bootstrap" 
            data-componentKey="fileDom">
               
            </div>
            `
        );
        //点击事件，修改属性
        clickChangeAttrs !== false && $(outerDom).off('click.addClickChoose').on('click.addClickChoose',Assist.addClickChoose);
        //拖拽事件
        dropAddComponent !== false && require("feature").bindDropEventToOrtumItem(outerDom);

        let ortum_component_properties = customProps ? customProps : Assist.deepClone(component_properties);


        //生成uuid
        ortum_component_properties.data.uuid || (ortum_component_properties.data.uuid = Assist.getUUId());
        outerDom.attr("ortum_uuid",ortum_component_properties.data.uuid)

        //设定name
        customName && (ortum_component_properties.data.name = customName);
        ortum_component_properties.data.name || (ortum_component_properties.data.name = Assist.timestampName('file'));
        let nameArr = ortum_component_properties.data.name.split("_");
        if(nameSuffix && createJson){
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1] + nameSuffix;
        }else{
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1]
        }


        $(outerDom).append($(`
            <div class="input-group">
                <div class="custom-file ${!ortum_component_properties.data.showFileName && 'custom-file-flexGrow'}">
                    <input type="file" class="${ortum_component_properties.data.cssClass} ${!ortum_component_properties.data.labelBindInput ? 'ortum_display_NONE' : ''}" 
                    name="${ortum_component_properties.data.name}" 
                    ${ortum_component_properties.data.formName ? "data-formname="+ortum_component_properties.data.formName : ""}
                    ${ortum_component_properties.data.title ? "title="+ortum_component_properties.data.title : ""}
                    ${ortum_component_properties.data.uploadUrl ? "data-uploadurl="+ortum_component_properties.data.uploadUrl : ""}
                    ${ortum_component_properties.data.multiple ? "multiple" : ""}
                    id="${ortum_component_properties.data.id ? ortum_component_properties.data.id : ortum_component_properties.data.name}" 
                    accept="${ortum_component_properties.data.accept}" 
                    />
                    <label class="custom-file-label ${!ortum_component_properties.data.showFileName && 'ortum_file_hideName'}" data-browse="${ortum_component_properties.data.browse}" 
                    ${ortum_component_properties.data.labelBindInput ? "for="+(ortum_component_properties.data.id || ortum_component_properties.data.name) : ""}>
                    ${ortum_component_properties.data.labelName}</label>
                </div>
                <div class="input-group-append" style="margin-left:0;display: none" id="ortum_file_append_${ortum_component_properties.data.uuid}">
                    <button class="btn btn-outline-secondary ortumPreviewFile_btn ${!ortum_component_properties.data.showPrevBtn && 'ortum_display_NONE'}" type="button" style="border-color: #ced4da;" 
                        id="ortumPreviewFile_${ortum_component_properties.data.uuid}">${ortum_component_properties.data.previewName || "预览"}</button>
                    <button class="btn btn-outline-secondary ortumDownFile_btn ${!ortum_component_properties.data.showDownBtn && 'ortum_display_NONE'}" type="button" style="border-color: #ced4da;" 
                        id="ortumDownFile_${ortum_component_properties.data.uuid}">${ortum_component_properties.data.downName || "下载"}</button>
                    <button class="btn btn-outline-secondary ortumDelFile_btn ${!ortum_component_properties.data.showDelBtn && 'ortum_display_NONE'}" type="button" style="border-color: #ced4da;" 
                        id="ortumDelFile_${ortum_component_properties.data.uuid}" >${ortum_component_properties.data.delName || "删除"}</button>
                </div>
            </div>
            <div class="progress" style="height: 5px;margin-top:5px;display:none">
                <div class="progress-bar" role="progressbar" style="width:0%" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
            </div>
        `))

        //修改编辑的属性
        if(Array.isArray(ortum_component_properties.data.attributesArr)){
            ortum_component_properties.data.attributesArr.forEach(function(item){
                outerDom.find("*[name="+ ortum_component_properties.data.name +"]").attr(item.label,item.value);
            });
        }

        //scriptDom
        let scriptDom ='';
        let scriptStr = "";
        //$("*[ortum_uuid=${ortum_component_properties.data.uuid}]").find("input").eq(0).off("change.automatic").on("change.automatic",ortum_bootstrap_file_uploadFile_${ortum_component_properties.data.uuid.replace(/-/g,"")})
        //$("#ortumDelFile_'+ ortum_component_properties.data.uuid +'").off("click.ortum").on("click.ortum",'+ ortum_component_properties.data.ortumDelFile +');' : ''
        //${(ortum_component_properties.data.ortumDownFile && typeof ortum_component_properties.data.ortumDownFile === "function") ? '$("#ortumDownFile_'+ ortum_component_properties.data.uuid +'").off("click.ortum").on("click.ortum",'+ ortum_component_properties.data.ortumDownFile +');' : ''}
        //${(ortum_component_properties.data.ortumPreviewFile && typeof ortum_component_properties.data.ortumPreviewFile === "function") ? '$("#ortumPreviewFile_'+ ortum_component_properties.data.uuid +'").off("click.ortum").on("click.ortum",'+ ortum_component_properties.data.ortumPreviewFile +');' : ''}
        if(createJson){
            scriptStr = `
            let ortum_bootstrap_file_changeLabelName_${ortum_component_properties.data.uuid.replace(/-/g,"")} = ${changeLabelName.toString()};
            ${ortum_component_properties.data.showFileName} && $("*[ortum_uuid=${ortum_component_properties.data.uuid}]").find("input").eq(0).off("change.changeLabelname").on("change.changeLabelname",ortum_bootstrap_file_changeLabelName_${ortum_component_properties.data.uuid.replace(/-/g,"")});
            ${ortum_component_properties.data.onCallBack ? "let ortum_bootstrap_file_onCallBack_"+ ortum_component_properties.data.uuid.replace(/-/g,"") +" = " + ortum_component_properties.data.onCallBack+";" : ''} 
            ${ortum_component_properties.data.onSuccess ? "let ortum_bootstrap_file_onSuccess_"+ ortum_component_properties.data.uuid.replace(/-/g,"") +" = " + ortum_component_properties.data.onSuccess+";" : ''} 
            ${ortum_component_properties.data.onError ? "let ortum_bootstrap_file_onError_"+ ortum_component_properties.data.uuid.replace(/-/g,"") +" = " + ortum_component_properties.data.onError+";" : ''} 
            ${(ortum_component_properties.data.ortumDelFile && typeof ortum_component_properties.data.ortumDelFile === "function") ? 
                '$("body").off("click.ortumDelFile_'+ ortum_component_properties.data.uuid +'").on("click.ortumDelFile_'+ortum_component_properties.data.uuid+'","*[ortum_uuid='+ ortum_component_properties.data.uuid +'] .ortumDelFile_btn",'+ ortum_component_properties.data.ortumDelFile +');' 
                : ''
            }
            ${(ortum_component_properties.data.ortumDownFile && typeof ortum_component_properties.data.ortumDownFile === "function") ?
                '$("body").off("click.ortumDownFile_'+ ortum_component_properties.data.uuid +'").on("click.ortumDownFile_'+ortum_component_properties.data.uuid+'","*[ortum_uuid='+ ortum_component_properties.data.uuid +'] .ortumDownFile_btn",'+ ortum_component_properties.data.ortumDownFile +');'
                : ''
            }
            ${(ortum_component_properties.data.ortumPreviewFile && typeof ortum_component_properties.data.ortumPreviewFile === "function") ?
                '$("body").off("click.ortumPreviewFile_'+ ortum_component_properties.data.uuid +'").on("click.ortumPreviewFile_'+ortum_component_properties.data.uuid+'","*[ortum_uuid='+ ortum_component_properties.data.uuid +'] .ortumPreviewFile_btn",'+ ortum_component_properties.data.ortumPreviewFile +');'
                : ''
            }
            ${(ortum_component_properties.data.onAfter && typeof ortum_component_properties.data.onAfter === "function") ? '!'+ortum_component_properties.data.onAfter+'($("*[ortum_uuid='+ ortum_component_properties.data.uuid +']").find("input").eq(0));' : ''}
            ${(ortum_component_properties.data.onChange && typeof ortum_component_properties.data.onChange === "function") ? '$("*[ortum_uuid='+ ortum_component_properties.data.uuid +']").find("input").eq(0).off("change.ortum").on("change.ortum",'+ ortum_component_properties.data.onChange +');' : ''}
            ${(ortum_component_properties.data.onLabelClick && typeof ortum_component_properties.data.onLabelClick === "function") ? '$("*[ortum_uuid='+ ortum_component_properties.data.uuid +']").find("label").eq(0).off("click.ortum").on("click.ortum",'+ ortum_component_properties.data.onLabelClick +');' : ''}
            `;
            if(ortum_component_properties.data.automatic && ortum_component_properties.data.uploadUrl && ortum_component_properties.data.formName){
                //绑定自动上传函数
                //change事件自动上传
                scriptStr +=`
                    let ortum_bootstrap_file_uploadFile_${ortum_component_properties.data.uuid.replace(/-/g,"")} = ${uploadFile.toString()};
                    $("body").off("change.automatic_${ortum_component_properties.data.uuid.replace(/-/g,"")}")
                    .on("change.automatic_${ortum_component_properties.data.uuid.replace(/-/g,"")}","div[ortum_uuid=${ortum_component_properties.data.uuid}] input[type=file]",ortum_bootstrap_file_uploadFile_${ortum_component_properties.data.uuid.replace(/-/g,"")})
                `
            }
            //生成script节点字符串
            scriptDom = $(`
                <script>${scriptStr}<\/script>
            `);
        }

        //TODO 后渲染的关联组件，可能无法正常控制权限
        switch (ortum_component_properties.data.authority) {
            case "hide":
                $(outerDom).addClass("ortum_display_NONE");
                //$("*[ortum_bindcomponentname="+ ortum_component_properties.data.name +"]").parents(".ortum_item").eq(0).addClass("ortum_display_NONE");
                break;
            case "edit":
                $(outerDom).removeClass("ortum_display_NONE");
                $(outerDom).find("input").removeAttr("readonly");
                $(outerDom).find("input").removeAttr("disabled");
                //$("*[ortum_bindcomponentname="+ ortum_component_properties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                break;
            case "readonly":
                $(outerDom).removeClass("ortum_display_NONE");
                $(outerDom).find("input").attr("readonly","readonly");
                $(outerDom).find("input").removeAttr("disabled");
                //$("*[ortum_bindcomponentname="+ ortum_component_properties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                break;
            case "disabled":
                $(outerDom).removeClass("ortum_display_NONE");
                $(outerDom).find("input").attr("readonly","readonly");
                $(outerDom).find("input").attr("disabled","disabled");
                //$("*[ortum_bindcomponentname="+ ortum_component_properties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                break;
            default:
                break;
        };

        //TODO 以后需要完善成根据设置成不同的 上传函数
        //给onChangeUpload绑定函数
        HasProperties && (ortum_component_properties.data.onChangeUpload = uploadFile)

        // if(!createJson){
        //     //绑定文件onchange
        //     ortum_component_properties.data.showFileName && $(outerDom).find('input').on('change.changeLabelname',ortum_component_properties.data.onChangeName)
        //     if(ortum_component_properties.data.automatic && ortum_component_properties.data.uploadUrl && ortum_component_properties.data.formName){
        //         $(outerDom).find('input').off("change.automatic").on("change.automatic",uploadFile)
        //     }else{
        //         $(outerDom).find('input').off("change.automatic")
        //     }
        // }  
        //插入script
        // scriptDom && !createJson && $(outerDom).append(scriptDom)

        //dom绑定property
        clickChangeAttrs !== false && $(outerDom).prop('ortum_component_properties',ortum_component_properties).prop('ortum_component_type',['Bootstrap','file']);
        if(parentDom){
            $(parentDom).append(outerDom);
        }else if(createJson){//生成json
            return {
                "name":ortum_component_properties.data.name,
                "html":outerDom && outerDom[0].outerHTML.replace(/\n/g,'').replace(/(\s)+/g," "),
                "title":(ortum_component_properties.data.title ? ortum_component_properties.data.title : ortum_component_properties.data.labelName),
                "script":scriptDom && scriptDom[0].outerHTML.replace(/\n/g,'').replace(/(\s)+/g," "),
                "componentProperties":(HasProperties ? Assist.jsonStringify(ortum_component_properties) : undefined),
                "ortumChildren":ortumChildren,
            }
        }else{
            return outerDom
        } 
    }

    /**
     * 功能：input事件，在这个事件上重置组件属性
     * @param {*} property 
     * @param {*} val 
     * @param {*} e 
     */
    let inputSetProperties = function(property,that,e){
        let val=$(that).val();
        let checked=$(that).prop('checked');

        if(!Global.ortum_edit_component || !Global.ortum_edit_component.comObj){
            return false;
        }
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        //判断值是否合理
        let vertifyPause =  evenProperties.verify && evenProperties.verify[property] && evenProperties.verify[property]['input'] && evenProperties.verify[property]["input"](globalComponent,e,val,checked);
        if(vertifyPause){
            return false;
        }

        //更新到dom属性上
        // evenProperties.data[property] = val;
        switch(property){
            case "previewName":
                $(globalComponent).find("*[id^=ortumPreviewFile]").eq(0).text(val)
                break;
            case "downName":
                $(globalComponent).find("*[id^=ortumDownFile]").eq(0).text(val)
                break;
            case "delName":
                $(globalComponent).find("*[id^=ortumDelFile]").eq(0).text(val)
                break;
            case "verification":
                //TODO 验证
                console.log(val)
                break;
            case "uploadUrl":
                $(globalComponent).find('input').eq(0).attr('data-uploadurl',val)
                break; 
            case "formName":
                $(globalComponent).find('input').eq(0).attr('data-formname',val)
                break; 
            case "cssClass":
                $(globalComponent).find('input').eq(0).attr('class',val)
                break; 
            case "labelName":
                $(globalComponent).find('label').eq(0).text(val)
                break; 
            case "browse":
                $(globalComponent).find('label').eq(0).attr('data-browse',val)
                break; 
            default:
                if(evenProperties.inputChange.indexOf(property) != -1){
                    $(globalComponent).find('input').eq(0).attr(property,val)
                }
                break;
        }
    }

    /**
     * 功能：blur事件
     * @param {*} property 
     * @param {*} val 
     * @param {*} e 
     */
    let blurSetProperties = function(property,that,e){
        let val=$(that).val();
        let checked=$(that).prop('checked');


        if(!Global.ortum_edit_component || !Global.ortum_edit_component.comObj){
            return false;
        }
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');
        
        //判断值是否合理
        let vertifyPause = evenProperties.verify && evenProperties.verify[property] && evenProperties.verify[property]["blur"] && evenProperties.verify[property]["blur"](globalComponent,e,val,checked);
        
        if(vertifyPause){
            return false;
        };

        //更新到dom属性上
        switch(property){
            case "multiple":case "automatic":case "showFileName":case "showPrevBtn":case "showDownBtn":case "showDelBtn":case "labelBindInput":
                evenProperties.data[property] = checked;
                break;
            default:
                evenProperties.data[property] = val;
                break;
        }
        
    }

    /**
     * 功能：click事件
     * @param {*} property 
     * @param {*} val 
     * @param {*} e 
     */
    let clickSetProperties = function(property,that,e){
        let val=$(that).val();
        let checked=$(that).prop('checked');

        if(!Global.ortum_edit_component || !Global.ortum_edit_component.comObj){
            return false;
        }
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');
        let uuid = evenProperties.data.uuid;
        
        //判断值是否合理
        let vertifyPause = evenProperties.verify && evenProperties.verify[property] && evenProperties.verify[property]["click"] && evenProperties.verify[property]["click"](globalComponent,e,val,checked);
        if(vertifyPause){
            return false;
        }
        // ${!ortum_component_properties.data.labelBindInput ? "for="+(ortum_component_properties.data.id || ortum_component_properties.data.name) : ''}"
        switch(property){
            case "labelBindInput":
                if(checked){
                    $(globalComponent).find('.custom-file-label').attr("for",evenProperties.data.id || evenProperties.data.name);
                    $(globalComponent).find('input').removeClass("ortum_display_NONE")
                }else{
                    $(globalComponent).find('.custom-file-label').removeAttr("for")
                    $(globalComponent).find('input').addClass("ortum_display_NONE")
                }
                break;
            case "showFileName":
                    if(checked){
                        $(globalComponent).find('.custom-file-label').eq(0).removeClass("ortum_file_hideName")
                        $(globalComponent).find('.custom-file').eq(0).removeClass("custom-file-flexGrow")
                    }else{
                        $(globalComponent).find('.custom-file-label').eq(0).addClass("ortum_file_hideName")
                        $(globalComponent).find('.custom-file').eq(0).addClass("custom-file-flexGrow")
                    }
                break;
            case "showPrevBtn":
                uuid && checked && $(globalComponent).find('#ortumPreviewFile_'+uuid).removeClass("ortum_display_NONE")
                uuid && !checked && $(globalComponent).find('#ortumPreviewFile_'+uuid).addClass("ortum_display_NONE")
                break;
            case "showDownBtn":
                uuid && checked && $(globalComponent).find('#ortumDownFile_'+uuid).removeClass("ortum_display_NONE")
                uuid && !checked && $(globalComponent).find('#ortumDownFile_'+uuid).addClass("ortum_display_NONE")
                break;
            case "showDelBtn":
                uuid && checked && $(globalComponent).find('#ortumDelFile_'+uuid).removeClass("ortum_display_NONE")
                uuid && !checked && $(globalComponent).find('#ortumDelFile_'+uuid).addClass("ortum_display_NONE")
                break;
            case "automatic":
                if(checked){
                    $(globalComponent).find('input').eq(0).off("change.automatic").on("change.automatic",uploadFile)
                }else{
                    $(globalComponent).find('input').eq(0).off("change.automatic")
                }
                break;
            case "authority":
                if(val=="hide"){//不可见
                    $(globalComponent).addClass("ortum_display_NONE");
                    //$("*[ortum_bindcomponentname="+ evenProperties.data.name +"]").parents(".ortum_item").eq(0).addClass("ortum_display_NONE");
                }
                if(val=="edit"){//可编辑
                    $(globalComponent).removeClass("ortum_display_NONE");
                    $(globalComponent).find("input").removeAttr("readonly");
                    $(globalComponent).find("input").removeAttr("disabled");
                    //$("*[ortum_bindcomponentname="+ evenProperties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                }
                if(val=="readonly"){//只读可点击
                    $(globalComponent).removeClass("ortum_display_NONE");
                    $(globalComponent).find("input").attr("readonly","readonly");
                    $(globalComponent).find("input").removeAttr("disabled");
                    //$("*[ortum_bindcomponentname="+ evenProperties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                }
                if(val=="disabled"){//只读且无法点击
                    $(globalComponent).removeClass("ortum_display_NONE");
                    $(globalComponent).find("input").attr("readonly","readonly");
                    $(globalComponent).find("input").attr("disabled","disabled");
                    //$("*[ortum_bindcomponentname="+ evenProperties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                }
                break;
            case "multiple":
                if(checked){
                    $(globalComponent).find('input').eq(0).attr(property,checked)
                }else{
                    $(globalComponent).find('input').eq(0).removeAttr(property)
                }
                break;
            default:
                if(evenProperties.clickChange.indexOf(property) != -1){
                    $(globalComponent).find('input').eq(0).attr(property,val)
                }
                break;
        }
    }


    /**
     * 功能：设置js
     */
    let ortumComponentSetJs = function(codeObj){
        if(!Global.ortum_edit_component || !Global.ortum_edit_component.comObj){
            return false;
        }
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        let setStr = "var ortum_BootstrapFile_setJs = {";
        if(evenProperties.data.onBefore){
            setStr += "\n//DOM渲染前的执行函数\nonBefore:"+ evenProperties.data.onBefore.toString() + ",";
        }else{
            setStr += "\n//DOM渲染前的执行函数\nonBefore:function(){},"
        }
        if(evenProperties.data.onAfter){
            setStr += "\n//DOM渲染后的执行函数\nonAfter:"+ evenProperties.data.onAfter.toString() + ",";
        }else{
            setStr += "\n//DOM渲染后的执行函数\nonAfter:function(that){},"
        }
        if(evenProperties.data.onLabelClick){
            setStr += "\n//label点击事件\nonLabelClick:"+ evenProperties.data.onLabelClick.toString() + ",";
        }else{
            setStr += "\n//label点击事件\nonLabelClick:function(){},"
        };
        if(evenProperties.data.ortumDelFile){
            setStr += "\n//删除\nortumDelFile:"+ evenProperties.data.ortumDelFile.toString() + ",";
        }else{
            setStr += "\n//删除\nortumDelFile:function(){},"
        }
        if(evenProperties.data.ortumPreviewFile){
            setStr += "\n//预览\nortumPreviewFile:"+ evenProperties.data.ortumPreviewFile.toString() + ",";
        }else{
            setStr += "\n//预览\nortumPreviewFile:function(){},"
        }

        if(evenProperties.data.ortumDownFile){
            setStr += "\n//下载\nortumDownFile:"+ evenProperties.data.ortumDownFile.toString() + ",";
        }else{
            setStr += "\n//下载\nortumDownFile:function(){},"
        }
        if(evenProperties.data.onChange){
            setStr += "\n//change事件\nonChange:"+ evenProperties.data.onChange.toString() + ",";
        }else{
            setStr += "\n//change事件\nonChange:function(){},"
        };
        if(evenProperties.data.onSuccess){
            setStr += "\n//上传成功后回调\nonSuccess:"+ evenProperties.data.onSuccess.toString() + ",";
        }else{
            setStr += "\n//上传成功后回调\nonSuccess:function(res,name){},"
        };
        if(evenProperties.data.onError){
            setStr += "\n//上传失败后回调\nonError:"+ evenProperties.data.onError.toString() + ",";
        }else{
            setStr += "\n//上传失败后回调\nonError:function(res,name){},"
        };

        if(evenProperties.data.onCallBack){
            setStr += "\n//上传后回调\nonCallBack:"+ evenProperties.data.onCallBack.toString() + ",";
        }else{
            setStr += "\n//上传后回调\nonCallBack:function(res,name){},"
        };




        setStr +="\n};";
        //格式化
        setStr = js_beautify(setStr,2);
        codeObj.setValue(setStr)
    };
    /**
     * 功能：保存js
     */
    let ortumComponentSaveJs = function(val){
        if(!Global.ortum_edit_component || !Global.ortum_edit_component.comObj){
            return false;
        }
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        let packer = new Packer;
        let valFormat = packer.pack(val, 0, 0); 
        try{
            eval(valFormat);
            evenProperties.data.onBefore = ortum_BootstrapFile_setJs.onBefore;
            evenProperties.data.onAfter = ortum_BootstrapFile_setJs.onAfter;
            evenProperties.data.ortumDelFile = ortum_BootstrapFile_setJs.ortumDelFile;
            evenProperties.data.ortumPreviewFile = ortum_BootstrapFile_setJs.ortumPreviewFile;
            evenProperties.data.ortumDownFile = ortum_BootstrapFile_setJs.ortumDownFile;
            evenProperties.data.onChange = ortum_BootstrapFile_setJs.onChange;
            evenProperties.data.onCallBack = ortum_BootstrapFile_setJs.onCallBack;
            evenProperties.data.onSuccess = ortum_BootstrapFile_setJs.onSuccess;
            evenProperties.data.onError = ortum_BootstrapFile_setJs.onError;
            evenProperties.data.onLabelClick = ortum_BootstrapFile_setJs.onLabelClick;
        }catch (e) {
            console.error("设置input的js有误，请重新设置");
        }
    };

    return {
        FileDom,

        inputSetProperties,
        blurSetProperties,
        // changeSetProperties,
        clickSetProperties,
        // keyDownSetProperties,
        // keyUpSetProperties,

        ortumComponentSetJs,
        ortumComponentSaveJs,

    }
})