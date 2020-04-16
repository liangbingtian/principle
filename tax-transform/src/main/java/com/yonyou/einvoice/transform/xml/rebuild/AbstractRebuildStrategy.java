package com.yonyou.einvoice.transform.xml.rebuild;

public abstract class AbstractRebuildStrategy {
  private boolean rebuild = false;
  private String rebuildPath;

  protected abstract void treeRebuild(XMLTreeNode treeNode, String path);

  public AbstractRebuildStrategy(String rebuildPath) {
    this.rebuildPath = rebuildPath;
  }

  public boolean isRebuild() {
    return rebuild;
  }

  public void setRebuild(boolean rebuild) {
    this.rebuild = rebuild;
  }

  public String getRebuildPath() {
    return rebuildPath;
  }

  public void setRebuildPath(String rebuildPath) {
    this.rebuildPath = rebuildPath;
  }
}
