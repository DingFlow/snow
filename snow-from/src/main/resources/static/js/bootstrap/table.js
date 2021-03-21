define(["require","assist","createDom","global","settings",'BootstrapAsider'], function(require, Assist, CreateDom, Global, Settings, BootstrapAsider){
    let component_properties = {
        data:{
            id:"",//id
            name:'',//name
            title:"",//设置dom的title属性，一般与labelName一致
            verification:"",//校验
            authority:"edit",//权限
            cssClass:"ortum_bootstrap_tableDom",//table 的 css类
            theadCssClass:'ortum_bootstrap_thead',

            tfootCssClass:"ortum_bootstrap_tfoot",
            tfootTrCssClass:"",
            tfootTdCssClass:"ortum_bootstrap_td",

            tbodyCssClass:'ortum_bootstrap_tbody',
            theadTrCssClass:'',

            tbodyTrCssClass:"",
            thCssClass:"ortum_bootstrap_th",
            tdCssClass:"ortum_bootstrap_td",
            showThead:true,
            showTbody:true,
            showTfoot:true,

            theadColumnsArr:[
                /*[
                    {
                        "type":"order",
                        "headHtml":"<span>序号</span>",
                        "rowspan":1,
                        "colspan":1,
                    },
                    {
                        "headHtml":"<span>年份</span>",
                        "rowspan":1,
                        "colspan":1,
                    },
                    {
                        "headHtml":"<span>投资金额</span>",
                        "rowspan":1,
                        "colspan":1,
                    },
                    {
                        "type":"act",//手动新增和删除
                        "headHtml":"<span>操作</span>",
                        "rowspan":1,
                        "colspan":1,
                    },
                ]*/
            ],//thead信息
            tbodyColumnsArr:[
                [
                    {
                        "type": "order",
                        "rowspan":3,
                        "colspan": 1
                    },
                    {
                        "rowspan": 1,
                        "colspan": 1,
                        "frame": "Bootstrap",
                        "componentKey": "inputDom",
                    },
                    {
                        "rowspan": 1,
                        "colspan": 1,
                        "frame": "Bootstrap",
                        "componentKey": "inputDom",
                    },
                    {
                        // "type":"act",
                        "rowspan": 3,
                        "colspan": 1,
                    }
                ],
                [
                    {
                        "rowspan": 1,
                        "colspan": 1,
                        "frame": "Bootstrap",
                        "componentKey": "inputDom",
                    },
                    {
                        "rowspan": 1,
                        "colspan": 1,
                        "frame": "Bootstrap",
                        "componentKey": "inputDom",
                    },
                ],
                [
                    {
                        "rowspan": 1,
                        "colspan": 1,
                        "frame": "Bootstrap",
                        "componentKey": "inputDom",
                    },
                    {
                        "rowspan": 1,
                        "colspan": 1,
                        "frame": "Bootstrap",
                        "componentKey": "inputDom",
                    },
                ]
            ],//tbody信息
            tfootColumnsArr:[
                /*[
                    {
                        "rowspan":1,
                        "colspan":1,
                        "frame":"Bootstrap",
                        "componentKey":"inputDom",
                    },
                    {
                        "rowspan":1,
                        "colspan":1,
                        "frame":"Bootstrap",
                        "componentKey":"inputDom",
                    },
                ],
                [
                    {
                        "rowspan":1,
                        "colspan":1,
                        "frame":"Bootstrap",
                        "componentKey":"inputDom",
                    },
                    {
                        "rowspan":1,
                        "colspan":1,
                        "frame":"Bootstrap",
                        "componentKey":"inputDom",
                    },
                ],*/
            ],

            onBefore:"",
            onAfter:"",
            onAddlineAfter:"",//新增之后的回调
            onAddlineBefore:"",//新增之前的回调
            onDellineAfter:"",//删除之前的回调
            onDellineBefore:"",//删除之后的回调

            uuid: "",
            attributesArr:[],//属性数组
        },
        inputChange:["id","name","title","tfootCssClass","tfootTrCssClass","tfootTdCssClass","verification","cssClass","theadCssClass","tbodyCssClass","theadTrCssClass","tbodyTrCssClass","thCssClass","tdCssClass"],//input事件修改值
        clickChange:["authority","showThead","showTbody","showTfoot"],
        purview:{//属性编辑权限
            id:3,//id
            name:3,//name
            title:3,//设置dom的title属性，一般与labelName一致

            verification:3,//校验
            authority:3,//权限
            cssClass:3,//table 的 css类

            tfootCssClass:3,
            tfootTrCssClass:3,
            tfootTdCssClass:3,

            theadCssClass:3,
            tbodyCssClass:3,
            tbodyTrCssClass:3,
            theadTrCssClass:3,
            thCssClass:3,
            tdCssClass:3,
            showThead:3,
            showTbody:3,
            showTfoot:3
        },
        dataShowType:{
            showThead:'switch',
            showTbody:'switch',
            showTfoot:'switch',
            authority:"checkbox",
        },
    }

    /**
     * 功能：创建bootstrap的input
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
     * @param {*} moreProps.ortumItemDom 编辑器中item对象
     * @param {*} moreProps.nameSuffix 名称后缀
     */
    let TableDom = function(parentDom,moreProps=null){
        let customProps = null;
        let clickChangeAttrs = true;
        let dropAddComponent = true;
        let customName = '';//自定义name

        let createJson = false;
        let HasProperties = false;
        let ortumChildren = null;
        let ortumItemDom = null;
        let nameSuffix = null;

        if(Assist.getDetailType(moreProps) == "Object"){
            customProps = (Assist.getDetailType(moreProps.customProps) == "Object" ? moreProps.customProps : null);
            moreProps.createJson !== null && moreProps.createJson !== undefined && (createJson =moreProps.createJson);
            moreProps.HasProperties !== null && moreProps.HasProperties !== undefined && (HasProperties =moreProps.HasProperties);
            moreProps.clickChangeAttrs === false && (clickChangeAttrs = moreProps.clickChangeAttrs);
            moreProps.dropAddComponent === false && (dropAddComponent = moreProps.dropAddComponent);
            moreProps.ortumChildren !== null && moreProps.ortumChildren !== undefined && (ortumChildren = moreProps.ortumChildren);
            moreProps.customName !== null && moreProps.customName !== undefined && (customName =moreProps.customName);
            moreProps.ortumItemDom !== null && moreProps.ortumItemDom !== undefined && (ortumItemDom =moreProps.ortumItemDom);
            moreProps.nameSuffix !== null && moreProps.nameSuffix !== undefined && (nameSuffix = moreProps.nameSuffix);
        };

        let outerDom=$(
            `
            <div class="form-group ortum_item ortum_bootstrap_table" 
                data-frame="Bootstrap" 
                data-componentKey="tableDom"
            > 
            </div>
            `
        );
        //点击事件，修改属性
        clickChangeAttrs !== false && $(outerDom).off('click.addClickChoose').on('click.addClickChoose',Assist.addClickChoose);
        //拖拽事件
        dropAddComponent !== false && require("feature").bindDropEventToOrtumItem(outerDom);

        let ortum_component_properties = (customProps ? customProps : Assist.deepClone(component_properties));

        //生成uuid
        ortum_component_properties.data.uuid || (ortum_component_properties.data.uuid = Assist.getUUId());
        outerDom.attr("ortum_uuid",ortum_component_properties.data.uuid);

        //设定name
        customName && (ortum_component_properties.data.name = customName);
        ortum_component_properties.data.name || (ortum_component_properties.data.name = Assist.timestampName('table'));
        let nameArr = ortum_component_properties.data.name.split("_");
        if(nameSuffix && createJson){
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1] + nameSuffix;
        }else{
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1]
        }

        let tableDom = $(`
            <table
            ${ortum_component_properties.data.id ? "id="+ortum_component_properties.data.id : '' } 
            name="${ortum_component_properties.data.name}" 
            class="${ortum_component_properties.data.cssClass}" 
            ></table>
        `);

        //修改编辑的属性
        if(Array.isArray(ortum_component_properties.data.attributesArr)){
            ortum_component_properties.data.attributesArr.forEach(function(item){
                tableDom.attr(item.label,item.value);
            });
        }

        //【tbody】
        let tbodyDom = $(`
            <tbody
            ${ortum_component_properties.data.tbodyCssClass ? "class="+ortum_component_properties.data.tbodyCssClass : '' } 
            ></tbody>
        `)
        if(!ortum_component_properties.data.showTbody){
            tbodyDom.addClass("ortum_display_NONE")
        };
        //新建tbody的tr属性
        let tdMoreProps = {
            trCssClass:ortum_component_properties.data.tbodyTrCssClass,
            tdCssClass:ortum_component_properties.data.tdCssClass,
            tableName:ortum_component_properties.data.name,
        };
        Assist.getDetailType(moreProps) == "Object" &&  Object.assign(tdMoreProps,moreProps);
        //将tr的所有td信息存放在tbody上
        !createJson && $(outerDom).prop("ortum-childrenArr-info",ortum_component_properties.data.tbodyColumnsArr)

        //创建tbody的tr
        let tbodyTrObj =BootstrapAsider.tableTbodyAddTrLine(tbodyDom,ortum_component_properties.data.tbodyColumnsArr,tdMoreProps);
        tbodyTrObj.forEach(function(item){
            $(tbodyDom).append(item);
        })


        //【thead】
        let theadDom = $(`
            <thead 
            ${ortum_component_properties.data.theadCssClass ? "class="+ortum_component_properties.data.theadCssClass : '' } 
            ></thead>
        `);

        if(!ortum_component_properties.data.showThead){
            theadDom.addClass("ortum_display_NONE")
        }
        let theadTrObj =BootstrapAsider.tableTheadAddTrLine(ortum_component_properties.data.theadColumnsArr,
            {
                trCssClass:ortum_component_properties.data.theadTrCssClass,
                thCssClass:ortum_component_properties.data.thCssClass,
            });
        theadTrObj.forEach(function(item){
            $(theadDom).append(item);
        })

        //【tfoot】
        let tfootDom = $(`
            <tfoot
                ${ortum_component_properties.data.tfootCssClass ? "class="+ortum_component_properties.data.tfootCssClass : '' } 
            ></tfoot>
        `);
        if(!ortum_component_properties.data.showTfoot){
            tfootDom.addClass("ortum_display_NONE");
        }
        let tfootTdMoreProps = {
            trCssClass:ortum_component_properties.data.tfootTrCssClass,
            tdCssClass:ortum_component_properties.data.tfootTdCssClass,
            tableName:ortum_component_properties.data.name,
        };
        Assist.getDetailType(moreProps) == "Object" &&  Object.assign(tfootTdMoreProps,moreProps);
        let tfootTrObj =BootstrapAsider.tableTfootAddTrLine(ortum_component_properties.data.tfootColumnsArr,tfootTdMoreProps);

        tfootTrObj.forEach(function(item){
            $(tfootDom).append(item);
        })

        $(tableDom).append(theadDom);
        $(tableDom).append(tbodyDom);
        $(tableDom).append(tfootDom);

        outerDom.append(tableDom);

        //dom绑定property
        clickChangeAttrs !== false && $(outerDom).prop('ortum_component_properties',ortum_component_properties).prop('ortum_component_type',['Bootstrap','table']);

        //script字符串
        let scriptStr = '';
        let scriptDom = ""
        let addlineTrInfo = [];
        //创建children
        let children = [];
        if(createJson){
            //创建tbody的tr模板
            let addlineTrInfoHTMLArr = BootstrapAsider.tableTbodyAddTrLine(null,ortum_component_properties.data.tbodyColumnsArr,tdMoreProps);
            addlineTrInfoHTMLArr.forEach(function(item){
                addlineTrInfo.push(item[0].outerHTML.replace(/\n/g,'').replace(/(\s)+/g," "))
            });

            //新增行的函数
            //可以根据table的name属性新增；也可以根据this新增，this必须是tbody下的tr的子元素，
            scriptStr +=`
                function ortumTableDom_addLine_${ortum_component_properties.data.name}(tableName="${ortum_component_properties.data.name}",tableVal=null,setValueFun=null){
                    ${typeof ortum_component_properties.data.onAddlineBefore == 'function' ? '!'+ortum_component_properties.data.onAddlineBefore.toString()+'(tableName);' : ''}
                
                    let tableDom;
                    let rendPowerArr=[];
                    tableName && (tableDom = $("table[name="+ tableName +"]"));
                    !tableName && (tableDom = $(this).parents("table").eq(0));
                    if(!tableDom.length){
                        console.error("缺少table信息");
                        return false;
                    };
                    let tdInfoArr =tableDom.parents(".ortum_item").eq(0).prop("ortum-childrenArr-info");
                    let addlineTr =tableDom.parents(".ortum_item").eq(0).prop("ortum_tbodyTr_info");
                    
                    /*对应的行*/
                    let tbodyFirstTr = tableDom.find("tbody tr:nth-of-type(1)");
                 
                    let maxRowspan = addlineTr.length || 1;
                    let tbodyTrLength = tableDom.find('tbody tr').length;
                    /*组号*/
                    let order = maxRowspan && (tbodyTrLength/maxRowspan);
                    
                    function ortum_BootstraptableDom_addLine(arr,trDom){
                        arr.forEach(function(item){
                            let itemDom = item;
                            if(item.childrenType == "choose"){
                                itemDom = item.chooseFun(null,order+1);
                            };
                            let nextHtml = $(itemDom.html);
                            
                            let hide = false;
                            let read = false;
                            let required = false;
                            let verifyInfo = "";
                            let tdHide = false;
                            nextHtml.find("*[name]").each(function(index2,item2){
                                let nameValArr = $(item2).attr("name") && $(item2).attr("name").split("_");
                                if(nameValArr && nameValArr.length && nameValArr[0] == "table"){
                                    /*********获取权限**********/
                                    if(tbodyFirstTr.length){
                                        let brotherDom = tbodyFirstTr.find("*[name="+ $(item2).attr("name") +"]").eq(0);
                                        brotherDom.parents(".ortum_item").eq(0).css("display") == "none" && (hide=true);
                                        brotherDom.parents("td").eq(0).css("display") == "none" && (tdHide=true);
                                        brotherDom.attr("disabled") && (read=true);
                                        brotherDom.attr("ortum_verify") && (verifyInfo=brotherDom.attr("ortum_verify"));
                                    };
                                    nameValArr[nameValArr.length - 1] = order+1;
                                };
                                $(item2).attr("name",nameValArr.join("_"));
                            });
                            if(itemDom.children && itemDom.children.length){
                                ortum_BootstraptableDom_addLine(itemDom.children,nextHtml);
                            };
                            $(trDom).find("ortum_children[data-order="+ itemDom.ortumChildren +"]").eq(0).replaceWith(nextHtml);
                            rendPowerArr.push({
                                dom:nextHtml,
                                name:nextHtml.find("*[name]").attr("name")
                            });
                            /*********设置权限**********/
                            if(hide){
                                nextHtml.hide();
                            };
                            if(tdHide){
                                nextHtml.parents("td").eq(0).hide();
                            };
                            if(read){
                                nextHtml.find("*[name]").attr("disabled","disabled");
                            };
                            if(required){
                                nextHtml.find("*[name]").attr("ortum_verify","required");
                            };
                            if(verifyInfo){
                                nextHtml.attr("ortum_authority","verifyInfo");
                            };
                            /*********设置值**********/
                            tableVal && (typeof setValueFun == "function") && rendPowerArr.forEach(function(item){
                                setValueFun(item.dom,item.name,tableVal);
                            });
                        });
                    };
                    let nextArrTr = [];
                    addlineTr.forEach(function(item,index){
                        let nextTr = $(item);
                        tbodyFirstTr = tableDom.find("tbody tr:nth-of-type("+ (index+1) +")");
                        ortum_BootstraptableDom_addLine(tdInfoArr,nextTr);
                        nextTr.find("td[data-type=order] span").text(order+1+index);
                        tableDom.find("tbody").eq(0).append(nextTr);
                        nextArrTr.push(nextTr);
                    });
                    if(nextArrTr.length == 1){
                        nextArrTr = nextArrTr[0];
                    };
                    ${typeof ortum_component_properties.data.onAddlineAfter == 'function' ? '!'+ortum_component_properties.data.onAddlineAfter.toString()+'(tableName,nextArrTr);' : ''}
                    return false;
                };
            `;
            //删除行的函数
            //可以根据table的name属性删除；也可以根据this删除，this必须是tbody下的tr的子元素，
            //根据table的name属性删除，必须提供order，表示删除第几行,如果是删除一组为单位的行，提供一组的第一行行号
            scriptStr+=`
                function ortumTableDom_delLine_${ortum_component_properties.data.name}(tableName="${ortum_component_properties.data.name}",order=false,act=false){
                    ${typeof ortum_component_properties.data.onDellineBefore == 'function' ? '!'+ortum_component_properties.data.onDellineBefore.toString()+'(tableName);' : ''}
                    let tableDom;
                    tableName && (tableDom = $("table[name="+ tableName +"]"));
                    !tableName && (tableDom = $(this).parents("table").eq(0));
                    if(!tableDom.length){
                        console.error("缺少table信息");
                        return false;
                    };
                    let tbodyDom = tableDom.find("tbody").eq(0);
                    if(act){
                        let tbodyDom = $(this).parents("tbody").eq(0);
                        let trFather = $(this).parents("tr").eq(0);
                        let rowspan = $(this).parents("td").eq(0).attr("ortum-grouplength") || $(this).parents("td").eq(0).attr("rowspan") || 1;
                        $(trFather).nextAll("tr").each(function(index,item){
                            if(index < (rowspan-1)){
                                return true;
                            };
                            $(this).find("*[name]").each(function(index2,item2){
                                let name = $(item2).attr("name");
                                let nameArr = name.split("_");
                                if(nameArr[0] === "table"){
                                    let oldOrder = nameArr.pop();
                                    nameArr.push(--oldOrder);
                                };
                                $(item2).attr("name",nameArr.join("_"));
                            });
                        });
                        $(trFather).nextAll("tr").each(function(index,item){
                            if(index < (rowspan-1)){
                                $(this).remove();
                                return true;
                            }else{
                                return false;
                            }
                        });
                        $(this).parents("tr").eq(0).remove();
                        $(tbodyDom).find("td[data-type=order]").each(function(index,item){
                            $(item).find("span").eq(0).text(index+1);
                        });
                    }else if(/^[1-9]\d*$/.test(order)){
                        let delTr = tbodyDom.find("tr:nth-of-type("+ order +")");
                        let tbodyDom = $(delTr).parents("tbody").eq(0);
                        let rowspan = $(delTr).find("td").eq(0).attr("ortum-grouplength") || $(delTr).find("td").eq(0).attr("rowspan") || 1;
                        $(delTr).nextAll("tr").each(function(index,item){
                            if(index < (rowspan-1)){
                                return true;
                            };
                            $(this).find("*[name]").each(function(index2,item2){
                                let name = $(item2).attr("name");
                                let nameArr = name.split("_");
                                if(nameArr[0] === "table"){
                                    let oldOrder = nameArr.pop();
                                    nameArr.push(--oldOrder);
                                };
                                $(item2).attr("name",nameArr.join("_"));
                            });
                        });
                        $(delTr).nextAll("tr").each(function(index,item){
                            if(index < (rowspan-1)){
                                $(this).remove();
                                return true;
                            }else{
                                return false;
                            }
                        });
                        $(this).parents("tr").eq(0).remove();
                        $(tbodyDom).find("td[data-type=order]").each(function(index,item){
                            $(item).find("span").eq(0).text(index+1);
                        });
                    };
                    ${typeof ortum_component_properties.data.onDellineAfter == 'function' ? '!'+ortum_component_properties.data.onDellineAfter.toString()+'(tableName);' : ''}
                };
            `;
            //点击和删除的按钮
            scriptStr+=`
                $("table[name=${ortum_component_properties.data.name}]").on("click.addline","td[data-type=act] .icon-jiahao",function(){
                    ortumTableDom_addLine_${ortum_component_properties.data.name}();
                });
                $("table[name=${ortum_component_properties.data.name}]").on("click.delete","td[data-type=act] .icon-shanchu",function(){
                    ortumTableDom_delLine_${ortum_component_properties.data.name}.call(this,false,false,true);
                });
            `;


            //创建tfoot的td的json信息
            ortumItemDom && $(ortumItemDom).find("tfoot").eq(0).find(".ortum_item").each(function(domIndex,domItem){

                if($(domItem).parents(".ortum_item")[0] !== $(ortumItemDom)[0]){
                    return true;
                };
                let frame= $(domItem).attr("data-frame");
                let componentKey = $(domItem).attr("data-componentKey");

                let rowIndex = $(domItem).parents("tr").eq(0).index();
                let colIndex = $(domItem).parents("td").eq(0).index();

                if(!ortum_component_properties.data.tfootColumnsArr[rowIndex] || !ortum_component_properties.data.tfootColumnsArr[rowIndex][colIndex]){
                    return true;
                }

                //创建组件的属性
                let createDomProp = Object.assign({},moreProps);
                createDomProp.customProps = $(domItem).prop('ortum_component_properties');
                createDomProp.ortumChildren = "tfoot"+colIndex+"-"+rowIndex;//插入第几个ortum_children
                createDomProp.HasProperties = HasProperties;
                createDomProp.ortumItemDom = $(domItem);
                let comDom = require("createDom")[Settings.menuListDataJSON[componentKey].createFn](null,frame,createDomProp);
                children.push(Object.assign({
                    "frame":frame,
                    "componentKey":componentKey,
                    "children":[],
                },comDom));
                //寻找子组件
                $(domItem).find(".ortum_item").each(function (sonIndex,sonItem) {
                    if($(sonItem).parents(".ortum_item")[0] !== $(domItem)[0]){
                        return true;
                    };

                    let ortum_component_type =  $(domItem).prop("ortum_component_type");
                    //首字母大写
                    let s = ortum_component_type[1].slice(0,1).toUpperCase();
                    let h = ortum_component_type[1].slice(1);
                    let ortumChildrenOrder = sonIndex;
                    if(require(ortum_component_type[0] + s + h).getOrtumChildrenOrder){
                        ortumChildrenOrder = require(ortum_component_type[0] + s + h).getOrtumChildrenOrder($(domItem),$(sonItem));
                    };

                    require("feature").getFormContentJson("dom",{
                        "dom":$(sonItem),
                        "parent":children[children.length-1],
                        "HasProperties":HasProperties,
                        "ortumChildren":ortumChildrenOrder,
                    });
                });
            });

            //创建tbody中td中组件的json信息
            ortumItemDom && $(ortumItemDom).find("tbody").eq(0).find(".ortum_item").each(function(domIndex,domItem){
                if($(domItem).parents(".ortum_item")[0] !== $(ortumItemDom)[0]){
                    return true;
                };
                let frame= $(domItem).attr("data-frame");
                let componentKey = $(domItem).attr("data-componentKey");
                let rowIndex = $(domItem).parents("tr").eq(0).index();//从0开始
                let tdParent = $(domItem).parents("td").eq(0);
                let colIndex = tdParent.index();//从0开始
                let tdType = tdParent.attr("data-type");
                let comDom;

                if(!ortum_component_properties.data.tbodyColumnsArr[rowIndex] || !ortum_component_properties.data.tbodyColumnsArr[rowIndex][colIndex]){
                    return true;
                }

                //创建组件的属性
                let createDomProp = Object.assign({},moreProps);
                createDomProp.customProps = $(domItem).prop('ortum_component_properties');
                createDomProp.HasProperties = HasProperties;
                createDomProp.customName = ortum_component_properties.data.name+"_"+colIndex+"-"+rowIndex  + "_" + 1;//1代表第一行
                createDomProp.ortumItemDom = $(domItem);

                switch (tdType){
                    case "order":
                        comDom ={
                            "html":`<span class="ortum_item ortum_item_custom">1</span>`,
                            "ortumChildren":colIndex+"-"+rowIndex,//插入第几个ortum_children
                        };
                        children.push(Object.assign({
                            "frame":null,
                            "componentKey":null,
                            "children":[],
                        },comDom));
                        break;
                    case "act":
                        createDomProp.ortumChildren= colIndex+"-"+rowIndex;//插入第几个ortum_children
                        //新增
                        let addComDom = require('createDom')[Settings.menuListDataJSON["iconButtonDom"].createFn](null,Global.ortum_createDom_frame,Object.assign({
                            "iconName":"icon-jiahao",
                        },createDomProp));
                        //删除
                        let delComDom = require('createDom')[Settings.menuListDataJSON["iconButtonDom"].createFn](null,Global.ortum_createDom_frame,Object.assign({
                            "iconName":"icon-shanchu",
                        },createDomProp));
                        comDom = {
                            "childrenType":"choose",
                            "chooseFun":function (parents,sureOrder=false) {
                                let order = 0;
                                if(sureOrder !== false){
                                    order = sureOrder;
                                }else{
                                    let tbodyDom = parents.find('tbody').eq(0);
                                    let maxRowspan = 1;
                                    $(tbodyDom).find("tr").eq(0).find("td").each(function(index,tdItem){
                                        if($(tdItem).attr("rowspan") && ($(tdItem).attr("rowspan")*1) > maxRowspan){
                                            maxRowspan = $(tdItem).attr("rowspan")*1;
                                        };
                                    });
                                    order = $(tbodyDom).find("tr").length / maxRowspan;
                                }
                                if(order == 1){
                                    return this.addComDom;
                                }else{
                                    return this.delComDom;
                                };
                            }.toString(),
                            "delComDom":delComDom,
                            "addComDom":addComDom,
                            "name":createDomProp.customName,
                            "title": "操作",
                        };
                        children.push(Object.assign({
                            "frame":null,
                            "componentKey":null,
                            "children":[],
                        },comDom));
                        break;
                    default:
                        if(frame && componentKey){
                            createDomProp.ortumChildren=colIndex+"-"+rowIndex;//插入第几个ortum_children
                            comDom =require("createDom")[Settings.menuListDataJSON[componentKey].createFn](null,frame,createDomProp);
                            children.push(Object.assign({
                                "frame":frame,
                                "componentKey":componentKey,
                                "children":[],
                            },comDom));
                        };
                        //寻找子组件
                        $(domItem).find(".ortum_item").each(function (sonIndex,sonItem) {
                            if($(sonItem).parents(".ortum_item")[0] !== $(domItem)[0]){
                                return true;
                            };
                            let ortum_component_type =  $(domItem).prop("ortum_component_type");
                            //首字母大写
                            let s = ortum_component_type[1].slice(0,1).toUpperCase();
                            let h = ortum_component_type[1].slice(1);
                            let ortumChildrenOrder = sonIndex;
                            if(require(ortum_component_type[0] + s + h).getOrtumChildrenOrder){
                                ortumChildrenOrder = require(ortum_component_type[0] + s + h).getOrtumChildrenOrder($(domItem),$(sonItem));
                            };

                            // let ortumChildrenOrder = $(sonItem).parent().attr("data-order");
                            // !/[\d]+$/.test(ortumChildrenOrder) && (ortumChildrenOrder = sonIndex);

                            require("feature").getFormContentJson("dom",{
                                "dom":$(sonItem),
                                "parent":children[children.length-1],
                                "HasProperties":HasProperties,
                                "ortumChildren":ortumChildrenOrder,
                            });
                        });
                        break;
                }
            });

            if(ortum_component_properties.data.onAfter && typeof ortum_component_properties.data.onAfter === "function"){
                scriptStr += '!'+ortum_component_properties.data.onAfter+'($("input[name='+ ortum_component_properties.data.name +']").eq(0));'
            }

            scriptStr && (scriptDom = $(`<script>${scriptStr}</script>`));
        }

        //创建的对象返回
        if(parentDom){
            $(parentDom).append(outerDom);
        }else if(createJson){//生成json
            return {
                "name":ortum_component_properties.data.name,
                "title":(ortum_component_properties.data.title ? ortum_component_properties.data.title : ortum_component_properties.data.labelName),
                "html":outerDom[0].outerHTML.replace(/\n/g,'').replace(/(\s)+/g," "),
                "ortum_tbodyTr_info":addlineTrInfo,
                "script":scriptDom[0].outerHTML.replace(/\n/g,'').replace(/(\s)+/g," "),
                "children":children,
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
            case "verification":
                //TODO 验证
                console.log(val)
                break;
            case "tfootCssClass":
                $(globalComponent).find("tfoot").attr('class',val);
                break;
            case "tfootTrCssClass":
                $(globalComponent).find("tfoot").eq(0).find("tr").attr('class',val);
                break;
            case "tfootTdCssClass":
                $(globalComponent).find("tfoot").eq(0).find("td").attr('class',val);
                break;
            case "thCssClass":
                $(globalComponent).find("th").attr('class',val);
                break;
            case "tdCssClass":
                $(globalComponent).find("tbody").eq(0).find("td").attr('class',val);
                break;
            case "theadTrCssClass":
                $(globalComponent).find('thead').eq(0).find("tr").attr('class',val);
                break;
            case "tbodyTrCssClass":
                $(globalComponent).find('tbody').eq(0).find("tr").attr('class',val);
                break;
            case "tbodyCssClass":
                $(globalComponent).find('tbody').eq(0).attr('class',val);
                break;
            case "theadCssClass":
                $(globalComponent).find('tbody').eq(0).attr('class',val);
                break;
            case "cssClass":
                $(globalComponent).find('table').eq(0).attr('class',val);
                break;
            default:
                if(evenProperties.inputChange.indexOf(property) != -1){
                    $(globalComponent).find('table').eq(0).attr(property,val);
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
            case "showTbody":case "showThead":case "showTfoot":
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
        // evenProperties.data[property] = val;
        switch(property){
            //TODO
            case "authority":
                if(val=="hide"){//不可见
                }
                if(val=="edit"){//可编辑
                }
                if(val=="readonly"){//只读可点击
                }
                if(val=="disabled"){//只读且无法点击
                }
                break;

            case "showTbody":
                checked && $(globalComponent).find("tbody").removeClass("ortum_display_NONE");
                !checked && $(globalComponent).find("tbody").addClass("ortum_display_NONE");
                break;
            case "showTfoot":
                checked && $(globalComponent).find("tfoot").removeClass("ortum_display_NONE");
                !checked && $(globalComponent).find("tfoot").addClass("ortum_display_NONE");
                break;
            case "showThead":
                checked && $(globalComponent).find("thead").removeClass("ortum_display_NONE");
                !checked && $(globalComponent).find("thead").addClass("ortum_display_NONE");
                break;
            default:
                if(evenProperties.clickChange.indexOf(property) != -1){
                    $(globalComponent).find('table').eq(0).attr(property,val)
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

        let setStr = "var ortum_BootstrapInput_setJs = {";
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

        if(evenProperties.data.onAddlineBefore){
            setStr += "\n//新增之前的回调\nonAddlineBefore:"+ evenProperties.data.onAddlineBefore.toString() + ",";
        }else{
            setStr += "\n//新增之前的回调\nonAddlineBefore:function(tableName){},"
        }
        if(evenProperties.data.onAddlineAfter){
            setStr += "\n//新增之后的回调\nonAddlineAfter:"+ evenProperties.data.onAddlineAfter.toString() + ",";
        }else{
            setStr += "\n//新增之后的回调\nonAddlineAfter:function(tableName,trDom){},"
        }
        if(evenProperties.data.onDellineBefore){
            setStr += "\n//删除之前的回调\nonDellineBefore:"+ evenProperties.data.onDellineBefore.toString() + ",";
        }else{
            setStr += "\n//删除之前的回调\nonDellineBefore:function(tableName){},"
        }
        if(evenProperties.data.onDellineAfter){
            setStr += "\n//删除之后的回调\nonDellineAfter:"+ evenProperties.data.onDellineAfter.toString() + ",";
        }else{
            setStr += "\n//删除之后的回调\nonDellineAfter:function(tableName){},"
        }


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
            evenProperties.data.onBefore = ortum_BootstrapInput_setJs.onBefore;
            evenProperties.data.onAfter = ortum_BootstrapInput_setJs.onAfter;
            evenProperties.data.onAddlineBefore = ortum_BootstrapInput_setJs.onAddlineBefore;
            evenProperties.data.onAddlineAfter = ortum_BootstrapInput_setJs.onAddlineAfter;
            evenProperties.data.onDellineBefore = ortum_BootstrapInput_setJs.onDellineBefore;
            evenProperties.data.onDellineAfter = ortum_BootstrapInput_setJs.onDellineAfter;
        }catch (e) {
            console.error("设置input的js有误，请重新设置");
        }
    };



    /**
     * 保存对table的列设置
     */
    let saveTableColumns = function (val) {
        let globalComponent =Global.ortum_edit_component.comObj;
        let tbodyDom = globalComponent.find("tbody").eq(0);
        let theadDom = globalComponent.find("thead").eq(0);
        let tfootDom = globalComponent.find("tfoot").eq(0);
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        let packer = new Packer;
        let valFormat = packer.pack(val, 0, 0);
        try{
            eval(valFormat);

            let editTheadColumnArr = eval("theadColumns");
            let editTbodyColumnArr = eval("tbodyColumns");
            let editTfootColumnArr = eval("tfootColumns");

            //绑定属性先
            $(globalComponent).prop("ortum-childrenArr-info",editTbodyColumnArr);

            let tbodyTrObj =BootstrapAsider.tableTbodyAddTrLine(tbodyDom,editTbodyColumnArr,{
                trCssClass:evenProperties.data.tbodyTrCssClass,
                tdCssClass:evenProperties.data.tdCssClass,
                tableName:evenProperties.data.name,
            },settings="againEdit");
            $(tbodyDom).empty();//tbody 必须放在此处清空
            tbodyTrObj.forEach(function(item){
                $(tbodyDom).append(item);
            });

            let theadTrObj =BootstrapAsider.tableTheadAddTrLine(editTheadColumnArr,
                {
                    trCssClass:evenProperties.data.theadTrCssClass,
                    thCssClass:evenProperties.data.thCssClass,
                });
            $(theadDom).empty();//thead 必须放在此处清空
            theadTrObj.forEach(function(item){
                $(theadDom).append(item);
            })

            let tfootTrObj =BootstrapAsider.tableTfootAddTrLine(editTfootColumnArr,
                {
                    trCssClass:evenProperties.data.tfootTrCssClass,
                    tdCssClass:evenProperties.data.tfootTdCssClass,
                    tableName:evenProperties.data.name,
                });
            $(tfootDom).empty();//thead 必须放在此处清空
            $(tfootDom).append(tfootTrObj);

            //替换table上的属性值
            evenProperties.data.tfootColumnsArr = editTfootColumnArr;
            evenProperties.data.tbodyColumnsArr = editTbodyColumnArr;
            evenProperties.data.theadColumnsArr = editTheadColumnArr;

        }catch (e) {
            console.error(e);
            console.error("编辑table的column信息错误");
        }
    };
    /**
     * js设置table的列信息
     * @returns {boolean}
     */
    let setTableColumns = function () {
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        Global.ortum_codemirrorJS_setVal = function(codeObj){
            // let tableArr = [];
            // for(let item of evenProperties.data.tableColumnsArr){
            //     let pushItem = {};
            //     for(let key in item){
            //         if(item.hasOwnProperty(key)){
            //             key !== "customProps" && (pushItem[key] = item[key]);
            //         }
            //     };
            //     tableArr.push(pushItem)
            // }

            let theadArr = [];
            evenProperties.data.theadColumnsArr.forEach(function (itemArr,indexArr) {
                let pushArr = [];
                itemArr.forEach(function (item,index) {
                    let pushItem = {};
                    for(let key in item){
                        if(item.hasOwnProperty(key)){
                            key !== "customProps" && (pushItem[key] = item[key]);
                        }
                    };
                    pushArr.push(pushItem);
                })
                theadArr.push(pushArr)
            });

            let tbodyArr = [];
            evenProperties.data.tbodyColumnsArr.forEach(function (itemArr,indexArr) {
                let pushArr = [];
                itemArr.forEach(function (item,index) {
                    let pushItem = {};
                    for(let key in item){
                        if(item.hasOwnProperty(key)){
                            key !== "customProps" && (pushItem[key] = item[key]);
                        }
                    };
                    pushArr.push(pushItem);
                })
                tbodyArr.push(pushArr)
            });

            let tfootArr = [];
            evenProperties.data.tfootColumnsArr.forEach(function (itemArr,indexArr) {
                let pushArr = [];
                itemArr.forEach(function (item,index) {
                    let pushItem = {};
                    for(let key in item){
                        if(item.hasOwnProperty(key)){
                            key !== "customProps" && (pushItem[key] = item[key]);
                        }
                    };
                    pushArr.push(pushItem);
                })
                tfootArr.push(pushArr)
            });

            //格式化
            let theadArrStr = js_beautify(JSON.stringify(theadArr),2);
            let tbodyArrStr = js_beautify(JSON.stringify(tbodyArr),2);
            let tfootArrStr = js_beautify(JSON.stringify(tfootArr),2);

            codeObj.setValue(`//编辑thead的列信息\nvar theadColumns = ${theadArrStr};\n//编辑tbody的列信息\nvar tbodyColumns = ${tbodyArrStr};\n//编辑tfoot的列信息\nvar tfootColumns = ${tfootArrStr};
                `)
        }

        $('#ortum_top_dialog_xl').modal({
            "backdrop":"static",
            "keyboard":false,
        });
        //编辑js保存后执行的函数
        Global.ortum_codemirrorJS_save = saveTableColumns;
        $("#ortum_top_model_xl_content").load("/from/interfacecodemirror",function(){
            $('#ortum_top_model_xl_wait').hide();
        });
        return false;
    }

    /**
     * 功能：td中的组件被替换后
     */
    let sonOrtumItemNew = function(tdDom,replaceItem,otherParems={}){
        if(!tdDom){
            Assist.dangerTip("缺少td节点");
            return false;
        }
        let ItemProperties =replaceItem ? $(replaceItem).prop("ortum_component_properties") : null;
        let componentKey = replaceItem ? $(replaceItem).attr("data-componentKey") : otherParems.componentKey;
        let frame = replaceItem ? $(replaceItem).attr("data-frame") : otherParems.frame;
        let item = tdDom.prop("ortum_tableTd_item");

        //不存在构造属性
        if(!componentKey || !frame){
            Assist.dangerTip("缺少构造属性");
            return false;
        }

        if(componentKey == "tableDom"){
            Assist.dangerTip("插入或替换对象咱不支持table");
            return false;
        }

        // cellIndex  第几列，从0开始
        // rowIndex 表示整个table中的tr行数，从thead算起，从0开始计数
        // let cellIndex = tdDom[0].cellIndex;
        // let rowIndex = $(tdDom).parents("tr")[0].rowIndex;


        let cellOrder = $(tdDom).prop("ortum_tableTd_cellOrder");
        let rowOrder = $(tdDom).prop("ortum_tableTd_rowOrder");

        let rowIndex = (rowOrder*1)+1;//1为第一行

        if(tdDom.parents("tbody").length){
            let customName = tdDom.parents("table").eq(0).attr("name")+"_" + cellOrder + "-"+ rowOrder +"_" + rowIndex;
            let createDom = require('createDom')[Settings.menuListDataJSON[componentKey].createFn](null,frame,{
                customName:customName,
                customProps:ItemProperties,
            });
            $(tdDom).html(createDom);
            //将绑定的组件属性，绑定到td上
            item.customProps = $(createDom).prop("ortum_component_properties");
            item.uuid = $(createDom).prop("ortum_component_properties").data.uuid;
            item.frame =Global.ortum_createDom_frame;
            item.componentKey =componentKey;
        }else if(tdDom.parents("tfoot").length){
            let customName = tdDom.parents("table").eq(0).attr("name")+"_" + "tfoot" + "_" + cellOrder+"-"+rowOrder;
            let createDom = require('createDom')[Settings.menuListDataJSON[componentKey].createFn](null,Global.ortum_createDom_frame,{
                customName:customName,
                customProps:ItemProperties,
            });
            tdDom.html(createDom);
            //将绑定的组件属性，绑定到td上
            item.customProps = $(createDom).prop("ortum_component_properties");
            item.uuid = $(createDom).prop("ortum_component_properties").data.uuid;
            item.frame =Global.ortum_createDom_frame;
            item.componentKey =componentKey;
        }
    };
    /**
     * 功能：td中子组件被删除后
     */
    let sonOrtumItemDelete = function(tdDom){
        $(tdDom).append(require('BootstrapAsider').tableTdAddTip())//增加提示语
        let tdItem = $(tdDom).prop("ortum_tableTd_item");
        //去除item的组件属性
        delete tdItem.frame;
        delete tdItem.componentKey;
        delete tdItem.type;
        delete tdItem.customProps;
        delete tdItem.uuid;
    };

    return {
        TableDom,

        inputSetProperties,
        blurSetProperties,
        clickSetProperties,
        // changeSetProperties,
        // keyDownSetProperties,
        // keyUpSetProperties,

        setTableColumns,
        saveTableColumns,


        ortumComponentSetJs,
        ortumComponentSaveJs,

        sonOrtumItemNew,
        sonOrtumItemDelete,


    }
})