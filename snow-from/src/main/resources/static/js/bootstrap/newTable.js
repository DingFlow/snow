define(["require","assist","createDom","global","settings",'BootstrapAsider'], function(require, Assist, CreateDom, Global, Settings, BootstrapAsider){
    let component_properties = {
        data:{
            id:"",//id
            name:'',//name
            title:"",//设置dom的title属性，一般与labelName一致
            verification:"",//校验
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
                [
                    {
                        // "type":"order",
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
                        // "type":"act",//手动新增和删除
                        "headHtml":"<span>操作</span>",
                        "rowspan":1,
                        "colspan":1,
                    },
                ]
            ],//thead信息
            tbodyColumnsArr:[
                [
                    {
                        // "type": "order",
                        "rowspan":3,
                        "colspan": 1
                    },
                    {
                        "rowspan": 1,
                        "colspan": 1,
                    },
                    {
                        "rowspan": 1,
                        "colspan": 1,
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
                    },
                    {
                        "rowspan": 1,
                        "colspan": 1,
                    },
                ],
                [
                    {
                        "rowspan": 1,
                        "colspan": 1,
                    },
                    {
                        "rowspan": 1,
                        "colspan": 1,
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
        clickChange:["showThead","showTbody","showTfoot"],
        purview:{//属性编辑权限
            id:3,//id
            name:3,//name
            title:3,//设置dom的title属性，一般与labelName一致

            verification:3,//校验
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
        },
    }
    /**
     * 自带新增行逻辑
     * @param tableId
     * @param tableVal
     * @param setValueFun
     * @returns {boolean}
     */
    let ortumNewTableDom_addLine = function(event,tableId=undefined,tableVal=null,setValueFun=null){
        let tableDom;
        let rendPowerArr=[];
        tableId && (tableDom = $("table[id="+ tableId +"]"));
        !tableId && (tableDom = $(this).parents("table").eq(0));
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
                let verifyInfo = "";
                let authorityInfo = "";
                let tdHide = false;

                nextHtml.find("*[name]").each(function(index2,item2){
                    let nameVal = $(item2).attr("name");
                    let idVal = $(item2).attr("id");
                    let equalId = (nameVal === idVal) ? true:false;
                    let nameValArr = $(item2).attr("name") && $(item2).attr("name").split("_");
                    if(nameValArr && nameValArr.length && nameValArr.length>3){
                        /*********获取权限**********/
                        if(tbodyFirstTr.length){
                            let brotherDom = tbodyFirstTr.find("*[name="+ $(item2).attr("name") +"]").eq(0);
                            brotherDom.parents(".ortum_item").eq(0).attr("ortum_authority") && (authorityInfo = brotherDom.parents(".ortum_item").eq(0).attr("ortum_authority"));
                            brotherDom.parents("td").eq(0).css("display") == "none" && (tdHide=true);
                            brotherDom.attr("ortum_verify") && (verifyInfo=brotherDom.attr("ortum_verify"));
                        };
                        if(/^[\d]+$/.test(nameValArr[nameValArr.length - 1])){
                            nameValArr[nameValArr.length - 1] = order+1;
                            $(item2).attr("name",nameValArr.join("_"));
                            /*id与name值一致时,修改id与label*/
                            if(equalId){
                                $(item2).attr("id",nameValArr.join("_"));
                                nextHtml.find("label[for="+ idVal+"]").each(function(index3,item3){
                                    $(item3).attr("for",nameValArr.join("_"));
                                });
                            }
                        };
                        /*********设置权限**********/
                        if(authorityInfo){
                            switch (authorityInfo) {
                                case "hide":
                                    $(item2).parents(".ortum_item").eq(0).hide();
                                    break;
                                case "read":
                                    $(item2).attr("disabled","disabled");
                                    break;
                                case "edit":
                                    break;
                                case "required":
                                    break;
                                case "readAndReq":
                                    $(item2).attr("disabled","disabled");
                                    break;
                            };
                            $(item2).parents(".ortum_item").eq(0).attr("ortum_authority",authorityInfo);
                        }
                        if(tdHide){
                            $(item2).parents("td").eq(0).hide();
                        };
                        if(verifyInfo){
                            $(item2).attr("ortum_verify",verifyInfo);
                        };

                    };
                });
                $(trDom).find("ortum_children[data-order="+ itemDom.ortumChildren +"]").eq(0).replaceWith(nextHtml);
                /*********渲染子集children*********/
                if(itemDom.children && itemDom.children.length){
                    ortum_BootstraptableDom_addLine(itemDom.children,nextHtml);
                };
                rendPowerArr.push({
                    dom:nextHtml,
                    name:nextHtml.find("*[name]").attr("name")
                });
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
            nextTr.find("td[ortum_td_type=order] span").text(order+1+index);
            tableDom.find("tbody").eq(0).append(nextTr);
            nextArrTr.push(nextTr);
        });
        return nextArrTr;
    };

    /**
     * 自带删除行逻辑
     * @param tableId
     * @param order 删除的组号
     * @returns {boolean}
     */
    let ortumNewTableDom_delLine = function(event,tableId=undefined,group=false){
        let tableDom;
        tableId && (tableDom = $("table[id="+ tableId +"]"));
        !tableId && (tableDom = $(this).parents("table").eq(0));
        if(!tableDom.length){
            console.error("缺少table信息");
            return false;
        };
        let tbodyDom = tableDom.find("tbody").eq(0);
        /*判断是否为用户事件*/
        if(event && event.originalEvent){
            let tbodyDom = $(this).parents("tbody").eq(0);
            let trFather = $(this).parents("tr").eq(0);
            let rowspan = $(this).parents("td").eq(0).attr("ortum-grouplength") || $(this).parents("td").eq(0).attr("rowspan") || 1;
            $(trFather).nextAll("tr").each(function(index,item){
                if(index < (rowspan-1)){
                    return true;
                };
                let ctx = this;
                $(ctx).find("*[name]").each(function(index2,item2){
                    let nameVal = $(item2).attr("name");
                    let idVal = $(item2).attr("id");
                    let equalId = (nameVal === idVal) ? true:false;
                    let nameValArr = $(item2).attr("name") && $(item2).attr("name").split("_");
                    if(nameValArr && nameValArr.length && nameValArr.length>3){
                        let oldOrder = nameValArr.pop();
                        if(/^[\d]+$/.test(oldOrder)){
                            nameValArr.push(--oldOrder);
                            $(item2).attr("name",nameValArr.join("_"));
                            /*id与name值一致时,修改id与label*/
                            if(equalId){
                                $(item2).attr("id",nameValArr.join("_"));
                                $(ctx).find("label[for="+ idVal+"]").each(function(index3,item3){
                                    $(item3).attr("for",nameValArr.join("_"));
                                });
                            }
                        }
                    };
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
            $(tbodyDom).find("td[ortum_td_type=order]").each(function(index,item){
                $(item).find("span").eq(0).text(index+1);
            });
        }else if(/^[1-9]\d*$/.test(group)){
            let delTr = tbodyDom.find("tr:nth-of-type("+ group +")");
            let tbodyDom = $(delTr).parents("tbody").eq(0);
            let rowspan = $(delTr).find("td").eq(0).attr("ortum-grouplength") || $(delTr).find("td").eq(0).attr("rowspan") || 1;
            $(delTr).nextAll("tr").each(function(index,item){
                if(index < (rowspan-1)){
                    return true;
                };
                let ctx = this;
                $(ctx).find("*[name]").each(function(index2,item2){
                    let nameVal = $(item2).attr("name");
                    let idVal = $(item2).attr("id");
                    let equalId = (nameVal === idVal)?true:false;
                    let nameValArr = $(item2).attr("name") && $(item2).attr("name").split("_");
                    if(nameValArr && nameValArr.length && nameValArr.length>3){
                        let oldOrder = nameValArr.pop();
                        if(/^[\d]+$/.test(oldOrder)){
                            nameValArr.push(--oldOrder);
                            $(item2).attr("name",nameValArr.join("_"));
                            /*id与name值一致时,修改id与label*/
                            if(equalId){
                                $(item2).attr("id",nameValArr.join("_"));
                                $(ctx).find("label[for="+ idVal+"]").each(function(index3,item3){
                                    $(item3).attr("for",nameValArr.join("_"));
                                });
                            }
                        }
                    };
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
            $(tbodyDom).find("td[ortum_td_type=order]").each(function(index,item){
                $(item).find("span").eq(0).text(index+1);
            });
        };
    };

    /**
     * table的td添加拖拽提示语
     */
    let newTableTdAddTip = function(dataOrder){
        let tdWaitInsert = $(`
            <div class="ortum_table_td_waitInsert" >
                  <span>插入</span>              
            </div>
        `);
        return tdWaitInsert;
    }
    /**
     * 给td绑定拖拽事件
     * @param ele
     * @param item
     */
    let bindDropEventToBootstrapNewTable = function(ele,item){
        $(ele).on('dragover.firstbind',function(e){
            return false;
        });
        $(ele).on("dragenter.firstbind",function(e){//有拖动对象(包括自己作为拖动对象)进入我的领空时
            if(!Global.ortumNowDragObj)return false;
            //获取要创建的组件key
            let componentKey = $(Global.ortumNowDragObj).attr('data-key');
            //拖拽的是绘制区的组件
            let hasOrtumItem = $(Global.ortumNowDragObj).hasClass("ortum_item");

            if(hasOrtumItem){
                if(!$(Global.ortumNowDragObj).hasClass("ortum_bootstrap_table") && !$(Global.ortumNowDragObj).hasClass("ortum_bootstrap_newTable")){
                    if(!$(this).find(".ortum_item").length){
                        require("feature").ortumDragShadow(e,"enter",{That:this,addWay:"addClass"})
                        return false;
                    }
                }
            }else if(componentKey){
                if(componentKey !== "tableDom" && componentKey !== "newTableDom"){//如果拖拽上來的是grid,则不进行创建
                    if(!$(this).find(".ortum_item").length){
                        require("feature").ortumDragShadow(e,"enter",{That:this,addWay:"addClass"})
                        return false;
                    }
                }
            }
        });
        $(ele).on('drop.firstbind',function(e){
            require("feature").ortumDragShadow(e,"drop",{That:this})
            if(!Global.ortumNowDragObj){
                return false;
            };
            //获取要创建的组件key
            let componentKey = $(Global.ortumNowDragObj).attr('data-key');
            //拖拽的是绘制区的组件
            let hasOrtumItem = $(Global.ortumNowDragObj).hasClass("ortum_item");
            //已有组件替换
            if(hasOrtumItem){
                if(!$(Global.ortumNowDragObj).hasClass("ortum_bootstrap_table") && !$(Global.ortumNowDragObj).hasClass("ortum_bootstrap_newTable")){
                    if($(this).parents("tbody").length || $(this).parents("tfoot").length){
                        if(!$(this).find(".ortum_item").length){
                            sonOrtumItemNew($(this),Global.ortumNowDragObj ? $(Global.ortumNowDragObj) : $(Global.ortum_active_item));
                            //把拖拽对象制空
                            Global.ortumNowDragObj = null;
                            return false;
                        }
                    }
                }
            }else{
                if(!componentKey){//不存在对应key
                    return false;
                };
                if(!require('createDom')[Settings.menuListDataJSON[componentKey].createFn]){
                    Assist.dangerTip();
                    return false;
                }
                if(componentKey == "tableDom" || componentKey == "newTableDom"){
                    let onlyFrame = $(Global.ortumNowDragObj).attr("data-frame");
                    let useFrame = onlyFrame || Global.ortum_createDom_frame
                    let createDom = require('createDom')[Settings.menuListDataJSON[componentKey].createFn](null,useFrame);
                    let parentsItemLength = $(this).parents(".ortum_item").length;
                    if(parentsItemLength){
                        $(this).parents(".ortum_item").eq(parentsItemLength-1).before(createDom)
                    }else{
                        $(this).before(createDom)
                    }
                    //把拖拽对象制空
                    Global.ortumNowDragObj = null;
                    return false;
                }else{
                    if(!$(this).find(".ortum_item").length){//没有插入
                        let onlyFrame = $(Global.ortumNowDragObj).attr("data-frame");
                        let useFrame = onlyFrame || Global.ortum_createDom_frame
                        sonOrtumItemNew($(this),null,{
                            componentKey:componentKey,
                            frame:useFrame,
                        });
                        if($(this).parents("tbody").length || $(this).parents("tfoot").length){
                            //把拖拽对象制空
                            Global.ortumNowDragObj = null;
                            return false;
                        }
                    }
                }
            }
        })
    }

    /**
     * table的新增tr
     * @param tbodyDom
     * @param tbodyInfo
     * @param moreProps
     * @param settingAgain 是否默认为第一组
     * @returns {[]}
     */
    let newTableTbodyAddTrLine = function(tbodyDom=null,tbodyInfo, moreProps = null,settingAgain = false){
        let trCssClass='';
        let tdCssClass='';

        let bindDropEvent = true;//绑定拖拽事件
        let createJson = false;
        let prevGroupNum = 0;//上一组的组号(默认没有上一组)
        let maxRowspan = tbodyInfo.length;
        if(tbodyDom){
            let tbodyTrNum = 0;//tbody的tr数量
            tbodyTrNum = $(tbodyDom).find("tr").length;
            prevGroupNum = tbodyTrNum/maxRowspan;
        }

        //group 为组号，可能是一行为一组， 也可能是多行为一组;
        settingAgain && (prevGroupNum = 0);
        let group = prevGroupNum +1;

        if(Assist.getDetailType(moreProps) == "Object"){
            moreProps.trCssClass !== undefined && moreProps.trCssClass !== null && (trCssClass = moreProps.trCssClass);
            moreProps.tdCssClass !== undefined && moreProps.tdCssClass !== null && (tdCssClass = moreProps.tdCssClass);
            moreProps.bindDropEvent !== undefined && moreProps.bindDropEvent !== null && (bindDropEvent = moreProps.bindDropEvent);
            moreProps.createJson !== undefined && moreProps.createJson !== null && (createJson = moreProps.createJson);
        };
        let trInfoArr= [];

        if(Assist.getDetailType(tbodyInfo) == "Array"){
            tbodyInfo.forEach(function(itemArr,rowIndex){
                let trInfo = $(`<tr ${trCssClass ? "class="+trCssClass : '' } ></tr>`);
                trInfoArr.push(trInfo);
                itemArr.forEach(function (item,cellIndex) {
                    let colspan = 1;
                    let rowspan = 1;
                    (item.colspan && /(^[1-9]\d*$)/.test(item.colspan)) && (colspan = item.colspan);
                    (item.rowspan && /(^[1-9]\d*$)/.test(item.rowspan)) && (rowspan = item.rowspan);
                    let tdDom = $(`
                    <td 
                    colspan="${colspan}" 
                    rowspan="${rowspan}" 
                    class="${tdCssClass}">
                    </td>
                `);
                    //绑定拖拽事件
                    bindDropEvent && bindDropEventToBootstrapNewTable(tdDom,item);
                    bindDropEvent && tdDom.prop("ortum_tableTd_item",item);
                    bindDropEvent && tdDom.prop("ortum_tableTd_cellOrder",cellIndex);
                    bindDropEvent && tdDom.prop("ortum_tableTd_rowOrder",rowIndex);

                    let dataOrder = cellIndex + "-"+ rowIndex +"_"+ group;
                    //子组件的name后缀
                    tdDom.attr("ortum-name-suffix",dataOrder).attr("data-order",dataOrder);

                    //编辑table状态，并且有对应的组件
                    if(bindDropEvent && item.type){
                        tdDom.append(newTableTdAddTip()).addClass("ortum_bootstrap_td_waitInsert").attr("ortum-grouplength",maxRowspan);
                        switch (item.type) {
                            case "order":
                                tdDom.attr("ortum_td_type","order");
                                let hasOrtumItem = null;
                                if(item.uuid){
                                    hasOrtumItem = $("*[ortum_uuid="+ item.uuid +"]").eq(0);
                                    delete item.uuid
                                }else if(settingAgain){
                                    let customUUID= Assist.getUUId();
                                    hasOrtumItem = `<span class="ortum_item ortum_item_custom" ortum_uuid="${customUUID}">${group + (rowIndex*rowspan)}</span>`;
                                }

                                if(hasOrtumItem && hasOrtumItem.length && tdDom){
                                    tdDom.append(hasOrtumItem);
                                };
                                break;
                            case "act":
                                tdDom.attr("ortum_td_type","act");
                                if(group == 1){//第一组
                                    let hasOrtumItem = null;
                                    if(item.uuid){
                                        hasOrtumItem = $("*[ortum_uuid="+ item.uuid +"]").eq(0);
                                        delete item.uuid
                                    }else if(settingAgain){
                                        hasOrtumItem = require('createDom')[Settings.menuListDataJSON["iconButtonDom"].createFn](null,Global.ortum_createDom_frame,{
                                            "iconName":"icon-jiahao",
                                            "nameSuffix":"_"+dataOrder,
                                        });
                                    }
                                    if(hasOrtumItem && hasOrtumItem.length && tdDom){
                                        tdDom.append(hasOrtumItem)
                                    };

                                }else{
                                    let hasOrtumItem = null;
                                    if(item.uuid){
                                        hasOrtumItem = $("*[ortum_uuid="+ item.uuid +"]").eq(0);
                                        delete item.uuid
                                    }else if(settingAgain){
                                        hasOrtumItem = require('createDom')[Settings.menuListDataJSON["iconButtonDom"].createFn](null,Global.ortum_createDom_frame,{
                                            "iconName":"icon-shanchu",
                                            "nameSuffix":"_"+dataOrder,
                                        });
                                    }

                                    if(hasOrtumItem && hasOrtumItem.length && tdDom){
                                        tdDom.append(hasOrtumItem)

                                    };
                                }
                                break;
                            default:
                                console.error("type类型不正确")
                                break;
                        }
                    }else if(bindDropEvent){
                        tdDom.append(newTableTdAddTip()).addClass("ortum_bootstrap_td_waitInsert").attr("ortum-grouplength",maxRowspan);
                        let hasOrtumItem = null;
                        if(item.uuid){
                            hasOrtumItem = $("*[ortum_uuid="+ item.uuid +"]").eq(0);
                            delete item.uuid
                        }
                        if(hasOrtumItem && hasOrtumItem.length){
                            //绑定组件属性
                            tdDom.append(hasOrtumItem)
                        }
                    }else if(!bindDropEvent && createJson){//创建js
                        switch (item.type) {
                            case "order":
                                tdDom && (
                                    tdDom.append(`
                                        <ortum_children data-order="${dataOrder}"></ortum_children>
                                    `).attr("ortum_td_type","order").attr("ortum-grouplength",maxRowspan)
                                );
                                break;
                            case "act":
                                tdDom && (
                                    tdDom.append(`
                                        <ortum_children data-order="${dataOrder}"></ortum_children>
                                    `).attr("ortum_td_type","act").attr("ortum-grouplength",maxRowspan)
                                );
                                break;
                            default:
                                tdDom && (
                                    tdDom.append(`
                                        <ortum_children data-order="${dataOrder}"></ortum_children>
                                    `).attr("ortum-grouplength",maxRowspan)
                                );
                                break;
                        }
                    }
                    trInfo.append(tdDom);
                })
            });
        };
        return trInfoArr;
    };
    /**
     * 功能：table的thead新增行
     */
    let newTableTheadAddTrLine = function(thInfo,moreProps=null){
        let trCssClass='';
        let thCssClass='';
        if(Assist.getDetailType(moreProps) == "Object"){
            (moreProps.trCssClass !== undefined && moreProps.trCssClass !== null) && (trCssClass = moreProps.trCssClass);
            (moreProps.thCssClass !== undefined && moreProps.thCssClass !== null) && (thCssClass = moreProps.thCssClass);
        };
        let trInfoArr = [];
        if(Assist.getDetailType(thInfo) == "Array"){
            thInfo.forEach(function(itemArr,rowIndex){
                let trInfo = $(`
                    <tr
                    ${trCssClass ? "class="+trCssClass : '' } 
                    ></tr>
                `);
                trInfoArr.push(trInfo);
                itemArr.forEach(function(item,cellIndex){
                    let headHtml= "";
                    let type = '';
                    let colspan = 1;
                    let rowspan = 1;
                    (item.headHtml !== undefined && item.headHtml !== null) && (headHtml = item.headHtml);
                    (item.type !== undefined && item.type !== null) && (type = item.type);
                    (item.colspan && /(^[1-9]\d*$)/.test(item.colspan)) && (colspan = item.colspan);
                    (item.rowspan && /(^[1-9]\d*$)/.test(item.rowspan)) && (rowspan = item.rowspan);

                    //序号
                    if(type == "order"){
                        headHtml= "<span>序号</span>";
                    }
                    //操作
                    if(type == "act"){
                        headHtml= "<span>操作</span>";
                    };

                    $(trInfo).append(`
                        <th
                        colspan="${colspan}" 
                        rowspan="${rowspan}" 
                        ${thCssClass ? "class="+thCssClass : '' } 
                        > ${headHtml} </th>
                     `);
                });
            })
        }
        return trInfoArr;
    };

    /**
     * 创建tfoot
     * @param tbodyDom
     * @param tdInfo
     * @param moreProps
     * @returns {[]}
     */
    let newTableTfootAddTrLine = function(tdInfo,moreProps = null){
        let trCssClass = '';
        let tdCssClass = '';

        let bindDropEvent = true;//绑定拖拽事件
        let createJson = false;

        if(Assist.getDetailType(moreProps) == "Object"){
            moreProps.trCssClass !== undefined && moreProps.trCssClass !== null && (trCssClass = moreProps.trCssClass);
            moreProps.tdCssClass !== undefined && moreProps.tdCssClass !== null && (tdCssClass = moreProps.tdCssClass);
            moreProps.bindDropEvent !== undefined && moreProps.bindDropEvent !== null && (bindDropEvent = moreProps.bindDropEvent);
            moreProps.createJson !== undefined && moreProps.createJson !== null && (createJson = moreProps.createJson);
        };

        let trInfoArr = [];

        if(Assist.getDetailType(tdInfo) == "Array"){
            tdInfo.forEach(function(itemArr,rowIndex){
                let trInfo = $(`<tr ${trCssClass ? "class="+trCssClass : '' } ></tr>`);
                trInfoArr.push(trInfo);

                itemArr.forEach(function (item,cellIndex) {
                    let headColspan = 1;
                    (item.headColspan && /(^[1-9]\d*$)/.test(item.headColspan)) && (headColspan = item.headColspan);

                    let tdDom = $(`
                        <td 
                        colspan="${item.colspan}" 
                        rowspan="${item.rowspan}"
                        class="${tdCssClass}">
                        </td>
                    `);
                    //绑定拖拽事件
                    bindDropEvent && bindDropEventToBootstrapNewTable(tdDom,item);
                    bindDropEvent && tdDom.prop("ortum_tableTd_item",item);
                    bindDropEvent && tdDom.prop("ortum_tableTd_cellOrder",cellIndex);
                    bindDropEvent && tdDom.prop("ortum_tableTd_rowOrder",rowIndex);

                    let dataOrder = "tfoot-" + cellIndex + "-" + rowIndex;
                    //子组件的name后缀
                    tdDom.attr("ortum-name-suffix",dataOrder).attr("data-order",dataOrder);

                    //编辑table状态，并且有对应的组件
                    if(bindDropEvent){
                        tdDom.append(newTableTdAddTip()).addClass("ortum_bootstrap_td_waitInsert");
                        let hasOrtumItem = null;
                        if(item.uuid){
                            hasOrtumItem = $("*[ortum_uuid="+ item.uuid +"]").eq(0);
                            delete item.uuid;
                        }
                        if(hasOrtumItem && hasOrtumItem.length && tdDom){
                            //绑定组件属性
                            tdDom.append(hasOrtumItem)
                        }
                    }else if(!bindDropEvent && createJson){//创建js
                        tdDom && (
                            tdDom.append(`
                                <ortum_children data-order="${dataOrder}"></ortum_children>
                            `)
                        );
                    }
                    trInfo.append(tdDom);
                })
            });
        }
        return trInfoArr;
    };

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
    let NewTableDom = function(parentDom,moreProps=null){
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
            <div class="form-group ortum_item ortum_bootstrap_newTable" 
                data-frame="Bootstrap" 
                data-componentKey="newTableDom"
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
        ortum_component_properties.data.name || (ortum_component_properties.data.name = Assist.timestampName('newTable'));
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
        // !createJson && $(outerDom).prop("ortum-childrenArr-info",ortum_component_properties.data.tbodyColumnsArr)

        //创建tbody的tr,为一组
        let tbodyTrObj =newTableTbodyAddTrLine(tbodyDom,ortum_component_properties.data.tbodyColumnsArr,tdMoreProps);
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
        let theadTrObj =newTableTheadAddTrLine(ortum_component_properties.data.theadColumnsArr,
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
        let tfootTrObj =newTableTfootAddTrLine(ortum_component_properties.data.tfootColumnsArr,tfootTdMoreProps);

        tfootTrObj.forEach(function(item){
            $(tfootDom).append(item);
        })

        $(tableDom).append(theadDom);
        $(tableDom).append(tbodyDom);
        $(tableDom).append(tfootDom);

        outerDom.append(tableDom);

        //dom绑定property
        clickChangeAttrs !== false && $(outerDom).prop('ortum_component_properties',ortum_component_properties).prop('ortum_component_type',['Bootstrap','newTable']);

        //script字符串
        let scriptStr = '';
        let scriptDom = ""
        let addlineTrInfo = [];
        //创建children
        let children = [];

        if(createJson){
            //创建tbody的tr模板
            let addlineTrInfoHTMLArr = newTableTbodyAddTrLine(null,ortum_component_properties.data.tbodyColumnsArr,tdMoreProps);
            addlineTrInfoHTMLArr.forEach(function(item){
                addlineTrInfo.push(item[0].outerHTML.replace(/\n/g,'').replace(/(\s)+/g," "))
            });
            //处理theadColumnsArr  tbodyColumnsArr  tfootColumnsArr中ortum_uuid对应的ortum-item已经不存在问题
            /*function actItemUUid(arr){
                if(Array.isArray(arr)){
                    arr.forEach(function (item) {
                        if(Array.isArray(item)){
                            actItemUUid(item)
                        }else if(item && item.uuid){
                            let uuidObj = $("*[ortum_uuid="+ item.uuid +"]").eq(0);
                            if(!uuidObj.length){
                                item.uuid = undefined;
                            }
                        }
                    })
                }else if(arr && arr.uuid){
                    let uuidObj = $("*[ortum_uuid="+ arr.uuid +"]").eq(0);
                    if(!uuidObj.length){
                        arr.uuid = undefined;
                    }
                }
            }
            */


            //新增行的函数
            //可以根据table的Id属性新增；也可以根据this新增，this必须是tbody下的tr的子元素，
            scriptStr +=`
                function ortumNewTableDom_addLine_${ortum_component_properties.data.name}(event,tableId="${ortum_component_properties.data.id}",tableVal=null,setValueFun=null){
                    ${typeof ortum_component_properties.data.onAddlineBefore == 'function' ? '!'+ortum_component_properties.data.onAddlineBefore.toString()+'(event,tableId);' : ''}
                    let ortumNewTableDom_addLine = ${ortumNewTableDom_addLine.toString()};
                    let nextArrTr = ortumNewTableDom_addLine.call(this,event,tableId,tableVal,setValueFun);     
                    ${typeof ortum_component_properties.data.onAddlineAfter == 'function' ? '!'+ortum_component_properties.data.onAddlineAfter.toString()+'(event,tableId,nextArrTr);' : ''}
                    return false;
                };
            `;
            //删除行的函数
            //可以根据table的Id属性删除；也可以根据this删除，this必须是tbody下的tr的子元素，
            //根据table的Id属性删除，必须提供order，表示删除第几行,如果是删除一组为单位的行，提供一组的第一行行号
            scriptStr+=` 
                function ortumNewTableDom_delLine_${ortum_component_properties.data.name}(event,tableId="${ortum_component_properties.data.id}",order=false){
                    ${typeof ortum_component_properties.data.onDellineBefore == 'function' ? '!'+ortum_component_properties.data.onDellineBefore.toString()+'(event,tableId);' : ''}
                    let ortumNewTableDom_delLine = ${ortumNewTableDom_delLine.toString()};
                    ortumNewTableDom_delLine.call(this,event,tableId,order);
                    ${typeof ortum_component_properties.data.onDellineAfter == 'function' ? '!'+ortum_component_properties.data.onDellineAfter.toString()+'(event,tableId);' : ''}
                };
            `;
            //点击和删除的按钮
            scriptStr+=`
                $("table[name=${ortum_component_properties.data.name}]").on("click.addline","td[ortum_td_type=act] .icon-jiahao",function(event){
                    ortumNewTableDom_addLine_${ortum_component_properties.data.name}.call(this,event);
                });
                $("table[name=${ortum_component_properties.data.name}]").on("click.delete","td[ortum_td_type=act] .icon-shanchu",function(event){
                    ortumNewTableDom_delLine_${ortum_component_properties.data.name}.call(this,event,false,false);
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
                createDomProp.ortumChildren = "tfoot-"+colIndex+"-"+rowIndex;//插入第几个ortum_children
                let nameSuffixDom = $(domItem).parents("*[ortum-name-suffix]").eq(0);
                let nameSuffix;
                if(nameSuffixDom.length){
                    nameSuffix = nameSuffixDom.attr("ortum-name-suffix");
                    nameSuffix = "_"+nameSuffix;
                };
                createDomProp.nameSuffix = nameSuffix;
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
                let tdType = tdParent.attr("ortum_td_type");
                let comDom;

                if(!ortum_component_properties.data.tbodyColumnsArr[rowIndex] || !ortum_component_properties.data.tbodyColumnsArr[rowIndex][colIndex]){
                    return true;
                }

                //创建组件的属性
                let createDomProp = Object.assign({},moreProps);
                createDomProp.customProps = $(domItem).prop('ortum_component_properties');
                createDomProp.HasProperties = HasProperties;
                createDomProp.ortumItemDom = $(domItem);

                let ortumChildren = colIndex+"-"+rowIndex + "_" + 1;//插入第几个ortum_children
                createDomProp.ortumChildren = ortumChildren;//插入第几个ortum_children
                let nameSuffixDom = $(domItem).parents("*[ortum-name-suffix]").eq(0);
                let nameSuffix;
                if(nameSuffixDom.length){
                    nameSuffix = nameSuffixDom.attr("ortum-name-suffix");
                    nameSuffix = "_"+nameSuffix;
                };
                createDomProp.nameSuffix = nameSuffix;
                switch (tdType){
                    case "order":
                        comDom ={
                            "html":`<span class="ortum_item ortum_item_custom">1</span>`,
                            "ortumChildren":ortumChildren,
                        };
                        children.push(Object.assign({
                            "frame":null,
                            "componentKey":null,
                            "children":[],
                        },comDom));
                        break;
                    case "act":
                        //新增
                        let addComDom = require('createDom')[Settings.menuListDataJSON["iconButtonDom"].createFn](null,Global.ortum_createDom_frame,Object.assign({
                            "iconName":"icon-jiahao",
                        },createDomProp));
                        //删除
                        let delComDom = require('createDom')[Settings.menuListDataJSON["iconButtonDom"].createFn](null,Global.ortum_createDom_frame,Object.assign({
                            "iconName":"icon-shanchu",
                        },Object.assign({},createDomProp)));
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
                scriptStr += '!'+ortum_component_properties.data.onAfter+'("'+ ortum_component_properties.data.uuid +'","'+ ortum_component_properties.data.name +'");'
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

        let setStr = "var ortum_BootstrapNewTable_setJs = {";
        if(evenProperties.data.onBefore){
            setStr += "\n//DOM渲染前的执行函数\nonBefore:"+ evenProperties.data.onBefore.toString() + ",";
        }else{
            setStr += "\n//DOM渲染前的执行函数\nonBefore:function(){},"
        }
        if(evenProperties.data.onAfter){
            setStr += "\n//DOM渲染后的执行函数\nonAfter:"+ evenProperties.data.onAfter.toString() + ",";
        }else{
            setStr += "\n//DOM渲染后的执行函数\nonAfter:function(uuid,name){},"
        }

        if(evenProperties.data.onAddlineBefore){
            setStr += "\n//新增之前的回调\nonAddlineBefore:"+ evenProperties.data.onAddlineBefore.toString() + ",";
        }else{
            setStr += "\n//新增之前的回调\nonAddlineBefore:function(event,tableId){},"
        }
        if(evenProperties.data.onAddlineAfter){
            setStr += "\n//新增之后的回调\nonAddlineAfter:"+ evenProperties.data.onAddlineAfter.toString() + ",";
        }else{
            setStr += "\n//新增之后的回调\nonAddlineAfter:function(event,tableId,trDom){},"
        }
        if(evenProperties.data.onDellineBefore){
            setStr += "\n//删除之前的回调\nonDellineBefore:"+ evenProperties.data.onDellineBefore.toString() + ",";
        }else{
            setStr += "\n//删除之前的回调\nonDellineBefore:function(event,tableId){},"
        }
        if(evenProperties.data.onDellineAfter){
            setStr += "\n//删除之后的回调\nonDellineAfter:"+ evenProperties.data.onDellineAfter.toString() + ",";
        }else{
            setStr += "\n//删除之后的回调\nonDellineAfter:function(event,tableId){},"
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
            evenProperties.data.onBefore = ortum_BootstrapNewTable_setJs.onBefore;
            evenProperties.data.onAfter = ortum_BootstrapNewTable_setJs.onAfter;
            evenProperties.data.onAddlineBefore = ortum_BootstrapNewTable_setJs.onAddlineBefore;
            evenProperties.data.onAddlineAfter = ortum_BootstrapNewTable_setJs.onAddlineAfter;
            evenProperties.data.onDellineBefore = ortum_BootstrapNewTable_setJs.onDellineBefore;
            evenProperties.data.onDellineAfter = ortum_BootstrapNewTable_setJs.onDellineAfter;
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
            // $(globalComponent).prop("ortum-childrenArr-info",editTbodyColumnArr);

            let tbodyTrObj =newTableTbodyAddTrLine(tbodyDom,editTbodyColumnArr,{
                trCssClass:evenProperties.data.tbodyTrCssClass,
                tdCssClass:evenProperties.data.tdCssClass,
                tableName:evenProperties.data.name,
            },true);
            $(tbodyDom).empty();//tbody 必须放在此处清空
            tbodyTrObj.forEach(function(item){
                $(tbodyDom).append(item);
            });

            let theadTrObj =newTableTheadAddTrLine(editTheadColumnArr,
                {
                    trCssClass:evenProperties.data.theadTrCssClass,
                    thCssClass:evenProperties.data.thCssClass,
                });
            $(theadDom).empty();//thead 必须放在此处清空
            theadTrObj.forEach(function(item){
                $(theadDom).append(item);
            });

            let tfootTrObj =newTableTfootAddTrLine(editTfootColumnArr,
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
            let theadArr = [];
            evenProperties.data.theadColumnsArr.forEach(function (itemArr,indexArr) {
                let pushArr = [];
                itemArr.forEach(function (item,index) {
                    let pushItem = {};
                    for(let key in item){
                        if(item.hasOwnProperty(key)){
                            pushItem[key] = item[key]
                        }
                    };
                    pushArr.push(pushItem);
                })
                theadArr.push(pushArr);
            });

            let tbodyArr = [];
            evenProperties.data.tbodyColumnsArr.forEach(function (itemArr,indexArr) {
                let pushArr = [];
                itemArr.forEach(function (item,index) {
                    let bindOrtumItem = $(globalComponent).find("tbody").eq(0).find("tr:nth-of-type("+ (indexArr+1) +")").find("td:nth-of-type("+ (index+1) +")").find(".ortum_item").eq(0);
                    let pushItem = {};
                    for(let key in item){
                        if(item.hasOwnProperty(key)){
                            pushItem[key] = item[key];
                        }
                    };
                    if(bindOrtumItem.length){
                        pushItem["uuid"] = $(bindOrtumItem).attr("ortum_uuid");
                    };
                    pushArr.push(pushItem);
                })
                tbodyArr.push(pushArr)
            });

            let tfootArr = [];
            evenProperties.data.tfootColumnsArr.forEach(function (itemArr,indexArr) {
                let pushArr = [];
                itemArr.forEach(function (item,index) {
                    let bindOrtumItem = $(globalComponent).find("tfoot").eq(0).find("tr:nth-of-type("+ (indexArr+1) +")").find("td:nth-of-type("+ (index+1) +")").find(".ortum_item").eq(0);
                    let pushItem = {};
                    for(let key in item){
                        if(item.hasOwnProperty(key)){
                            pushItem[key] = item[key]
                        };
                    };
                    if(bindOrtumItem.length){
                        pushItem["uuid"] = $(bindOrtumItem).attr("ortum_uuid");
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
        $("#ortum_top_model_xl_content").load("/from/interface/codemirror",function(){
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
        // let ItemProperties =replaceItem ? $(replaceItem).prop("ortum_component_properties") : null;
        let componentKey = replaceItem ? $(replaceItem).attr("data-componentKey") : otherParems.componentKey;
        let frame = replaceItem ? $(replaceItem).attr("data-frame") : otherParems.frame;
        let item = tdDom.prop("ortum_tableTd_item");

        //不存在构造属性
        if(!componentKey || !frame){
            Assist.dangerTip("缺少构造属性");
            return false;
        }
        if(componentKey == "tableDom" || componentKey == "newTableDom"){
            Assist.dangerTip("插入或替换对象咱不支持table");
            return false;
        }

        if(tdDom.parents("tbody").length){
            // let nameSuffix = "_" + cellOrder + "-"+ rowOrder +"_" + rowIndex;
            let createDom = null;
            if($(replaceItem).length){
                createDom = $(replaceItem).eq(0);
            }else{
                createDom = require('createDom')[Settings.menuListDataJSON[componentKey].createFn](null,frame,{});
            }
            $(tdDom).append(createDom);


        }else if(tdDom.parents("tfoot").length){
            let createDom = null;
            if($(replaceItem).length){
                createDom = $(replaceItem).eq(0);
            }else{
                createDom = require('createDom')[Settings.menuListDataJSON[componentKey].createFn](null,frame,{});
            }
            tdDom.append(createDom);
        }
    };
    /**
     * 功能：td中子组件被删除后
     */
    let sonOrtumItemDelete = function(tdDom){
        // let cellOrder = $(tdDom).prop("ortum_tableTd_cellOrder");
        // let rowOrder = $(tdDom).prop("ortum_tableTd_rowOrder");
        //
        // let isTbody = $(tdDom).parents("tr").eq(0).parent("tbody").eq(0);
        // let isTfoot = $(tdDom).parents("tr").eq(0).parent("tfoot").eq(0);
        // let dataOrder = undefined;

        // if(isTbody){
        //     dataOrder = cellOrder + "-"+ rowOrder +"_"+ 1;
        // }else if(isTfoot){
        //     dataOrder = "tfoot-"+cellOrder + "-"+ rowOrder;
        // }
        // dataOrder && $(tdDom).append(newTableTdAddTip())//增加提示语

        let tdItem = $(tdDom).prop("ortum_tableTd_item");
        //去除item的组件属性
        delete tdItem.type;
        delete tdItem.uuid;
    };

    return {
        NewTableDom,

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