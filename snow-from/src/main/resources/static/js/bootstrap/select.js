define(["require","assist","createDom","global"],function(require,Assist,CreateDom,Global){
    let component_properties = {
        data:{
            id:"",//id
            name:'',//name
            defaultVal:["123"],//默认值
            verification:"",//校验
            authority:"edit",//权限
            cssClass:"custom-select col",//css类
            hideLabel:true,//是否隐藏标签
            labelName:"名称",//标签名称
            labelPosition:"rowLeft",//标签位置
            // labelWidth:"",//标签宽度
            labelCSS:"col-form-label col-2",//标签css类
            useRemote:false,//服务器获取option
            getOptions:null,
            serverUrl:"",//服务器地址
            title:"",
            options:[
                /*{
                    value:"123",
                    name:"玫瑰",
                    selected:true,
                },
                {
                    value:"321",
                    name:"兰花",
                    // disabled:true,
                },
                {
                    value:"321",
                    name:"菊花",
                    // hide:true,
                },*/
            ],
            onBefore:"",//渲染之前的回调
            onAfter:"",//渲染之后的回调
            onChange:"",//change事件
            uuid: "",
            attributesArr:[],//属性数组

        },
        inputChange:["id","name","verification","cssClass","labelName","labelCSS","serverUrl","title"],//input事件修改值
        clickChange:["authority","hideLabel","labelPosition","useRemote"],
        purview:{//属性编辑权限
            id:3,//id
            name:3,
            // defaultVal:3,
            verification:3,
            authority:3,//权限
            cssClass:3,//css类
            hideLabel:3,//是否隐藏标签
            labelName:3,//标签名称
            labelPosition:3,//标签位置
            // labelWidth:1,//标签宽度
            labelCSS:3,//标签css类
            useRemote:3,
            serverUrl:3,
            title:3,
        },
        dataShowType:{
            hideLabel:"switch",
            useRemote:"switch",
            authority:"checkbox",
            labelPosition:"checkbox",
        },
    }

    /**
     * 功能：远端获取options
     */
    let getOptions = function(checked=false){
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');
        if(!evenProperties.data.useRemote || !checked)return;

        if(Assist.getDetailType(evenProperties.data.customGetOptions) == "Function"){
            evenProperties.data.customGetOptions(evenProperties.data.name,ortumReq)
        }
        /* OrtumReq.ortumReq({
            "url":evenProperties.data.serverUrl,
            "method":"POST",
            "data":fd,
            "success":(xhr,e)=>{
                require("assist").infoTip("上传成功。");
            },
            "error":(xhr,e)=>{
                require("assist").dangerTip("上传失败！");
            },
            progress:(xhr,e)=>{
                let pro = e.loaded/e.total * 100
                // console.log(pro);
                $(globalComponent).find(".progress-bar").eq(0).css("width", pro+"%")
                $(globalComponent).find(".progress-bar").eq(0).attr("aria-valuenow", pro)
            },
            final:(xhr,e)=>{
                setTimeout(function(){
                    $(globalComponent).find(".progress").eq(0).css("display","none")
                    $(globalComponent).find(".progress-bar").eq(0).css("width","0%")
                    $(globalComponent).find(".progress-bar").eq(0).attr("aria-valuenow", 0)
                },300)
            }
        }) */
    }

    /**
     * 功能：创建bootstrap的select
     * @param {*} parentDom 
     * @param {*} moreProps 一个json对象，
     * @param {*} moreProps.customProps 自定义属性
     * @param {*} moreProps.generateDom 函数也存在dom中
     * @param {*} moreProps.createJson 生成对应的json
     * @param {*} moreProps.HasProperties 保存组件的component_properties
     * @param {*} moreProps.clickChangeAttrs 是否允许修改点击属性（=== false的时候，去除点击修改属性）
     * @param {*} moreProps.dropAddComponent 拖拽添加组件
     * @param {*} moreProps.ortumChildren 插入<ortum_children>的data-order
     * @param {*} moreProps.customName 自定义name
     * @param {*} moreProps.nameSuffix 名称后缀
     */
    let SelectDom = function(parentDom,moreProps=null){
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
            moreProps.clickChangeAttrs === false && (clickChangeAttrs = moreProps.clickChangeAttrs);
            moreProps.createJson !== null && moreProps.createJson !== undefined && (createJson =moreProps.createJson);
            moreProps.HasProperties !== null && moreProps.HasProperties !== undefined && (HasProperties =moreProps.HasProperties);
            moreProps.dropAddComponent === false && (dropAddComponent = moreProps.dropAddComponent);
            moreProps.ortumChildren !== null && moreProps.ortumChildren !== undefined && (ortumChildren = moreProps.ortumChildren);
            moreProps.customName !== null && moreProps.customName !== undefined && (customName =moreProps.customName);
            moreProps.nameSuffix !== null && moreProps.nameSuffix !== undefined && (nameSuffix = moreProps.nameSuffix);
        }

        let outerDom=$(
            `
            <div class="form-group ortum_item row ortum_bootstrap_select" 
                data-frame="Bootstrap" 
                data-componentKey="selectDom" 
                >
               
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
        //设定nameselect
        customName && (ortum_component_properties.data.name = customName);
        ortum_component_properties.data.name || (ortum_component_properties.data.name = Assist.timestampName('select'));
        let nameArr = ortum_component_properties.data.name.split("_");
        if(nameSuffix && createJson){
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1] + nameSuffix;
        }else{
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1]
        }


        
        //控制标签
        if(ortum_component_properties.data.hideLabel){
            ortum_component_properties.data.labelCSS.indexOf("ortum_display_NONE") ==-1 ? (ortum_component_properties.data.labelCSS+= " ortum_display_NONE") : '';
        }else{
            switch(ortum_component_properties.data.labelPosition){
                case "topLeft":case "topRight":
                    $(outerDom).removeClass('row');
                    break;
                case "rowLeft":
                    $(outerDom).addClass('row');
                    break;
                default:
                    break;
            }
        }
        //生成selectDom
        let selectDom = $(`
            <select class="${ortum_component_properties.data.cssClass}" 
            ${ortum_component_properties.data.title ? "title="+ortum_component_properties.data.title : '' } 
            id="${ortum_component_properties.data.id}" 
            name="${ortum_component_properties.data.name}" >
            </select>
        `);



        //修改编辑的属性
        if(Array.isArray(ortum_component_properties.data.attributesArr)){
            ortum_component_properties.data.attributesArr.forEach(function(item){
                selectDom.attr(item.label,item.value);
            });
        };




        
        //scriptDom
        let scriptDom ='';
        let scriptStr = "";

        if(createJson){
            scriptStr +=`
                ${(ortum_component_properties.data.onChange && typeof ortum_component_properties.data.onChange === "function") ? '$("*[ortum_uuid='+ ortum_component_properties.data.uuid +']").find("select").eq(0).off("change.ortum").on("change.ortum",'+ ortum_component_properties.data.onChange +');' : ''}
                ${(ortum_component_properties.data.onAfter && typeof ortum_component_properties.data.onAfter === "function") ? '!'+ortum_component_properties.data.onAfter+'($("*[ortum_uuid='+ ortum_component_properties.data.uuid +']").find("select").eq(0),"'+ ortum_component_properties.data.name +'");' : ''}
            `;
            if(ortum_component_properties.data.useRemote && Assist.getDetailType(ortum_component_properties.data.customGetOptions) == "Function"){
                //远程获取option
                scriptDom += `${ortum_component_properties.data.customGetOptions.toString()};getOptions_${ortum_component_properties.data.name}("${ortum_component_properties.data.name}",ortumReq);
                    `; 
            }
            //生成script节点字符串
            scriptDom = $(`
                <script>${scriptStr}<\/script>
            `);
        }

        if(!ortum_component_properties.data.useRemote){
            //循环生成option
            for(let i=0;i<ortum_component_properties.data.options.length;i++){
                let choose = false;
                if(ortum_component_properties.data.defaultVal.indexOf(ortum_component_properties.data.options[i].value) != -1){
                    choose = true
                }
                selectDom.append(`
                    <option 
                    ${ortum_component_properties.data.options[i].selected ? "selected":""} 
                    ${ortum_component_properties.data.options[i].disabled ? "disabled":""} 
                    ${ortum_component_properties.data.options[i].hide ? "class='ortum_display_NONE'":""} 
                    value="${ortum_component_properties.data.options[i].value}">${ortum_component_properties.data.options[i].name}</option>
                `)
            }
        }
        //插入label
        $(outerDom).append($(`
            <label class="${ortum_component_properties.data.labelCSS}">${ortum_component_properties.data.labelName}</label>
        `))

        //插入select
        $(outerDom).append(selectDom);

        //TODO 后渲染的关联组件，可能无法正常控制权限
        switch (ortum_component_properties.data.authority) {
            case "hide":
                $(outerDom).addClass("ortum_display_NONE");
                // $("*[ortum_bindcomponentname="+ ortum_component_properties.data.name +"]").parents(".ortum_item").eq(0).addClass("ortum_display_NONE");
                break;
            case "edit":
                $(outerDom).removeClass("ortum_display_NONE");
                $(outerDom).find("select").removeAttr("readonly");
                $(outerDom).find("select").removeAttr("disabled");
                // $("*[ortum_bindcomponentname="+ ortum_component_properties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                break;
            case "readonly":
                $(outerDom).removeClass("ortum_display_NONE");
                $(outerDom).find("select").attr("readonly","readonly");
                $(outerDom).find("select").removeAttr("disabled");
                // $("*[ortum_bindcomponentname="+ ortum_component_properties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                break;
            case "disabled":
                $(outerDom).removeClass("ortum_display_NONE");
                $(outerDom).find("select").attr("readonly","readonly");
                $(outerDom).find("select").attr("disabled","disabled");
                // $("*[ortum_bindcomponentname="+ ortum_component_properties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                break;
            default:
                break;
        };

        //dom绑定property
        clickChangeAttrs !== false && $(outerDom).prop('ortum_component_properties',ortum_component_properties).prop('ortum_component_type',['Bootstrap','select']);
        if(parentDom){
            $(parentDom).append(outerDom);
        }else if(createJson){//生成json
            return {
                "name":ortum_component_properties.data.name,
                "html":outerDom && outerDom[0].outerHTML.replace(/\n/g,'').replace(/(\s)+/g," "),
                "title":(ortum_component_properties.data.title ? ortum_component_properties.data.title : ortum_component_properties.data.labelName),
                "script":scriptDom && scriptDom[0].outerHTML.replace(/\n/g,'').replace(/(\s)+/g," ").length >= 20 && scriptDom[0].outerHTML.replace(/\n/g,'').replace(/(\s)+/g," "),
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
            // case "defaultVal":
            //     $(globalComponent).find('input').eq(0).attr('value',val)
            //     $(globalComponent).find('input').eq(0).val(val)
            //     break;
            case "verification":
                //TODO 验证
                console.log(val)
                break;
            case "cssClass":
                // $(globalComponent).find('input').eq(0).addClass(val)
                $(globalComponent).find('select').eq(0).attr('class',val)
                break; 
            case "labelName":
                $(globalComponent).find('label').eq(0).text(val)
                break; 
            // case "labelWidth":
            //     $(globalComponent).find('label').eq(0).attr('width',val)
            //     break; 
            case "labelCSS":
                // $(globalComponent).find('label').eq(0).addClass(val)
                $(globalComponent).find('label').eq(0).attr('class',val)
                break;  
            default:
                if(evenProperties.inputChange.indexOf(property) != -1){
                    $(globalComponent).find('select').eq(0).attr(property,val)
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
        let vertifyPause = evenProperties.verify && evenProperties.verify[property] && evenProperties.verify[property]["blur"] && evenProperties.verify[property]["blur"](globalComponent,e,val);
        
        if(vertifyPause){
            return false;
        };

        //更新到dom属性上
        switch(property){
            case "hideLabel":case "useRemote":
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
        
        //判断值是否合理
        let vertifyPause = evenProperties.verify && evenProperties.verify[property] && evenProperties.verify[property]["click"] && evenProperties.verify[property]["click"](globalComponent,e,val);
        if(vertifyPause){
            return false;
        }
        //更新到dom属性上
        switch(property){
            case "useRemote":
                if(checked){
                    getOptions(checked);
                }else{
                    setSelectOptions([],true)
                }
                break;
            case "authority":
                if(val=="hide"){//不可见
                    $(globalComponent).addClass("ortum_display_NONE");
                    // $("*[ortum_bindcomponentname="+ evenProperties.data.name +"]").parents(".ortum_item").eq(0).addClass("ortum_display_NONE");
                }
                if(val=="edit"){//可编辑
                    $(globalComponent).removeClass("ortum_display_NONE");
                    $(globalComponent).find("select").removeAttr("readonly");
                    $(globalComponent).find("select").removeAttr("disabled");
                    // $("*[ortum_bindcomponentname="+ evenProperties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                }
                if(val=="readonly"){//只读可点击
                    $(globalComponent).removeClass("ortum_display_NONE");
                    $(globalComponent).find("select").attr("readonly","readonly");
                    $(globalComponent).find("select").removeAttr("disabled");
                    // $("*[ortum_bindcomponentname="+ evenProperties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                }
                if(val=="disabled"){//只读且无法点击
                    $(globalComponent).removeClass("ortum_display_NONE");
                    $(globalComponent).find("select").attr("readonly","readonly");
                    $(globalComponent).find("select").attr("disabled","disabled");
                    // $("*[ortum_bindcomponentname="+ evenProperties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                }
                break;
            case "hideLabel":
                if(checked){
                    $(globalComponent).find('label').eq(0).addClass('ortum_display_NONE');
                }else{
                    $(globalComponent).find('label').eq(0).removeClass('ortum_display_NONE');
                }
                evenProperties.data.labelCSS = $(globalComponent).find('label').eq(0).attr("class")
                $('#ortum_property_labelCSS').val($(globalComponent).find('label').eq(0).attr("class"))
                break; 
            case "labelPosition":
                //TODO 位置
                switch(val){
                    case "topLeft":
                        $(globalComponent).removeClass('row');
                        $(globalComponent).find('label').eq(0).removeClass(function (index, className) { 
                            return (className.match (/(?<=(^|\s))col(\S)*?(?=($|\s))/g) || []).join(' ');
                        });
                        $(globalComponent).find('select').eq(0).removeClass(function (index, className) {
                            return (className.match(/(?<=(^|\s))col(\S)*?(?=($|\s))/g) || []).join(' ');
                        });
                        $(globalComponent).find('label').eq(0).removeClass('ortum_boot_select_label_Right')
                        break;
                    case "topRight":
                        $(globalComponent).removeClass('row');
                        $(globalComponent).find('label').eq(0).removeClass(function (index, className) { 
                            return (className.match (/(?<=(^|\s))col(\S)*?(?=($|\s))/g) || []).join(' ');
                        });
                        $(globalComponent).find('select').eq(0).removeClass(function (index, className) {
                            return (className.match(/(?<=(^|\s))col(\S)*?(?=($|\s))/g) || []).join(' ');
                        });
                        $(globalComponent).find('label').eq(0).addClass('ortum_boot_select_label_Right')
                        break;
                    case "rowLeft":
                        let evenLabelCss = $('#ortum_property_labelCSS').val();
                        evenLabelCss = evenLabelCss.replace(/(?<=(^|\s))col(\S)*?(?=($|\s))/g,'')
                        $('#ortum_property_labelCSS').val(evenLabelCss + ' col-form-label col-2')
                        $(globalComponent).addClass('row');
                        $(globalComponent).find('label').eq(0).addClass('col-form-label col-2')
                        $(globalComponent).find('select').eq(0).addClass('col');
                        $(globalComponent).find('label').eq(0).removeClass('ortum_boot_select_label_Right')
                        break;
                    // case "rowRight":
                    //     $(globalComponent).addClass('row');
                    //     $(globalComponent).find('label').eq(0).addClass('col-form-label').addClass('col-2')
                    //     $(globalComponent).find('inputl').eq(0).addClass('col')
                    //     break;
                    default:
                        break;
                }
                $('#ortum_property_cssClass').val($(globalComponent).find('select').eq(0).attr("class"))
                $('#ortum_property_labelCSS').val($(globalComponent).find('label').eq(0).attr("class"))
                evenProperties.data.cssClass = $(globalComponent).find('select').eq(0).attr("class")
                evenProperties.data.labelCSS = $(globalComponent).find('label').eq(0).attr("class")
                break;    
            default:
                if(evenProperties.clickChange.indexOf(property) != -1){
                    $(globalComponent).find('select').eq(0).attr(property,val)
                }
                break;
        }
    }

    /**
     * 功能：新增选项
     * @param {*} newArr 
     */
    let setSelectOptions = function(newArr,setDefault=false){
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        //从远端获取
        if(evenProperties.data.useRemote && !Array.isArray(newArr)){
            try{
                //将字符串装成函数
                eval(newArr)
                //绑定获取option的自定义函数
                evenProperties.data.customGetOptions = eval("getOptions_" + evenProperties.data.name);
            }catch(err){
                evenProperties.data.customGetOptions = null;
                Assist.dangerTip("获取option函数编辑失败！",2000)
            }
            return;
        }
        //本地设置变量
        if(!evenProperties.data.useRemote && Array.isArray(newArr)){
            setDefault && (newArr = evenProperties.data.options);//设置默认值

            let selectDom = $(globalComponent).find('select').eq(0).empty();

            //默认选中值清空
            evenProperties.data.defaultVal = [];
            
            for(let i=0;i<newArr.length;i++){
                if(newArr[i].selected){
                    evenProperties.data.defaultVal.push(newArr[i].value)
                }
                let newDom = $(`
                <option 
                    ${newArr[i].selected ? "selected":""} 
                    ${newArr[i].disabled ? "disabled":""} 
                    ${newArr[i].hide ? "class='ortum_display_NONE'":""} 
                    value="${newArr[i].value}">${newArr[i].name}</option>
                `);
                $(selectDom).append(newDom);
            }
            evenProperties.data.options = newArr;
        } 
    }
    /**
     * 功能：回显选项
     */
    let showSelectOptions = function(){
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');
        //设置远端获取option函数
        if(evenProperties.data.useRemote){
            Global.ortum_codemirrorJS_setVal = function(codeObj){
                //函数字符串
                var funStr='';
                if(evenProperties.data.customGetOptions){
                    funStr=evenProperties.data.customGetOptions.toString();
                }else{
                    funStr = "function getOptions_"+ evenProperties.data.name +"(ortumDom,ortumAjax){\n\n\n\n\n}"
                }
                codeObj.setValue(`//函数名请勿编辑，只需编辑函数体内容\n//ortumDom是包裹select的节点name\n//ortumAjax是自定义的ajax请求\n${funStr}
                `)
            }

            $('#ortum_top_dialog_xl').modal({
                "backdrop":"static",
                "keyboard":false,
            })
            //编辑js保存后执行的函数
            Global.ortum_codemirrorJS_save = setSelectOptions;
            $("#ortum_top_model_xl_content").load("/from/interface/codemirror",function(){
                $('#ortum_top_model_xl_wait').hide();
            });
            return false;
        }
        //本地设置option
        if(!evenProperties.data.useRemote){
                //显示弹窗
            $('#ortum_top_dialog_lg').modal({
                "backdrop":"static",
            })
            //加载配置
            $("#ortum_top_model_lg_content").load("html/bootstrap/select_settings.html",function(){

                let itemsArr = evenProperties.data.options;
                let itemsLength = itemsArr.length;
                for(let i =1 ;i<itemsLength;i++){
                    $('#ortum_select_addLine').click();
                }
                itemsLength && $('#ortum_select_ModalLabel .ModalLabelTable').find('.ortum_order_dataTr').each(function(index,item){
                    $(item).find('.ortum_select_name').eq(0).val(itemsArr[index].name)
                    $(item).find('.ortum_select_value').eq(0).val(itemsArr[index].value)
                    if(itemsArr[index].selected){
                        $(item).find('.ortum_default_selected').eq(0).prop('checked',true)
                    }
                    if(itemsArr[index].disabled){
                        $(item).find('.ortum_default_disabled').eq(0).prop('checked',true)
                    }
                    if(itemsArr[index].hide){
                        $(item).find('.ortum_default_hide').eq(0).prop('checked',true)
                    }
                })

                $('#ortum_top_model_lg_wait').hide();
            });
            return false;
        }
    };

    /**
     * 功能：设置js
     */
    let ortumComponentSetJs = function(codeObj){
        if(!Global.ortum_edit_component || !Global.ortum_edit_component.comObj){
            return false;
        }
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        let setStr = "var ortum_BootstrapSelect_setJs = {";
        if(evenProperties.data.onBefore){
            setStr += "\n//DOM渲染前的执行函数\nonBefore:"+ evenProperties.data.onBefore.toString() + ",";
        }else{
            setStr += "\n//DOM渲染前的执行函数\nonBefore:function(){},"
        };
        if(evenProperties.data.onAfter){
            setStr += "\n//DOM渲染后的执行函数\nonAfter:"+ evenProperties.data.onAfter.toString() + ",";
        }else{
            setStr += "\n//DOM渲染后的执行函数\nonAfter:function(that,name){},"
        };
        if(evenProperties.data.onChange){
            setStr += "\n//change事件\nonChange:"+ evenProperties.data.onChange.toString() + ",";
        }else{
            setStr += "\n//change事件\nonChange:function(){},"
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
        };
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        let packer = new Packer;
        let valFormat = packer.pack(val, 0, 0); 
        try{
            eval(valFormat);
            evenProperties.data.onBefore = ortum_BootstrapSelect_setJs.onBefore;
            evenProperties.data.onAfter = ortum_BootstrapSelect_setJs.onAfter;
            evenProperties.data.onChange = ortum_BootstrapSelect_setJs.onChange;
        }catch (e) {
            console.error("设置input的js有误，请重新设置");
        };
    };

    return {
        SelectDom,

        setSelectOptions,
        showSelectOptions,

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