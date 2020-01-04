package com.shadow.springboot.application.domain.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * tree 抽象节点 树可继承这个节点
 *
 * @author tomsun28
 * @date 16:47 2018/3/20
 */
public abstract class BaseTreeNode {

    protected Integer rid;
    protected Integer parentId;
    protected List<BaseTreeNode> children = new ArrayList<>();

    public void addChilren(BaseTreeNode node) {
        this.children.add(node);
    }

    public List<BaseTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<BaseTreeNode> children) {
        this.children = children;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
