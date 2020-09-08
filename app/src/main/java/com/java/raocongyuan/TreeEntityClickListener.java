package com.java.raocongyuan;

public interface TreeEntityClickListener {
    public void onExpandChildren_first(Entity entity);
    public void onHideChildren_first(Entity entity);
    public void onExpandChildren_relations(Entity entity);
    public void onHideChildren_relations(Entity entity);
    public void onExpandChildren_properties(Entity entity);
    public void onHideChildren_properties(Entity entity);
}
