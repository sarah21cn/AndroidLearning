package Chapter10;

class MNA{
    private void f(){}

    class A{
        private void g(){}

        // 内部类不管嵌套多少层都可以透明地访问外部类的成员变量和方法,包括private
        public class B{
            void h(){
                f();
                g();
            }
        }
    }
}

public class MultiNestingAccess {

    public static void main(String args[]){
        // 多重嵌套内部类需要通过生成每一层的对象来得到
        MNA mna = new MNA();
        MNA.A mnaa = mna.new A();
        MNA.A.B mnaab = mnaa.new B();
        mnaab.h();
    }
}
