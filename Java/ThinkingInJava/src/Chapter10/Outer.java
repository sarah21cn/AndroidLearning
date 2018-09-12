package Chapter10;

interface ContentInterface {
    int value();
}

public class Outer {

    private int outerIndex;

    private void setOuterIndex(int i){
        this.outerIndex = i;
    }

    // public 所有类都能访问
    public class Inner{
        // 内部类持有对外部类的引用，通过.this获得
        public Outer getOuter(){
            return Outer.this;
        }

        // 内部类可以直接访问外部类的成员变量和成员方法
        // 其实是通过对外部类的引用来访问成员变量和方法, 相当于 Outer.this.outerIndex
        // 不管内部类被嵌套多少层，都能透明地访问所有它所嵌入的外围类的全部成员
        // 即使成员被定义为private，也可以被内部类直接访问
        private int innerIndex = outerIndex;

        // 相当于 Outer.this.setOuterIndex()
        public void setOutIndex(int i){
            setOuterIndex(i);
        }
    }

    // protected 只有子类和同一个包下的类可以访问
    // static 又称为嵌套类
    protected static class InnerStatic{
        // 只能访问外部类的static成员变量和方法
    }

    // private内部类不能被外部直接访问
    private class Contents implements ContentInterface {
        private int i = 10;

        @Override
        public int value() {
            return i;
        }
    }

    // private的内部类不能被外部访问，只能通过此方法访问private内部类的对象
    public Contents contents(){
        return new Contents();
    }

    public ContentInterface getContentInterface(){
        // 在方法作用域内的内部类
        class Contents implements ContentInterface{
            private int i = 20;

            @Override
            public int value() {
                return i;
            }
        }
        return new Contents();
    }

    public Contents getContent(){
        // 匿名内部类
        return new Contents(){
            // 匿名内部类也可以访问外部类的成员变量和方法
            private int i = outerIndex;

            @Override
            public int value() {
                return i;
            }
        };
    }

}



