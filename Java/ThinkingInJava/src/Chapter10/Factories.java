package Chapter10;

interface Service{
    void method1();
    void method2();
}


interface ServiceFactory{
    Service getService();
}

class Implementation1 implements Service{
    // 使用匿名内部类，构造方法可以声明为private
    private Implementation1(){ }

    @Override
    public void method1() {
        System.out.println("Implementation1 method1");
    }

    @Override
    public void method2() {
        System.out.println("Implementation1 method2");
    }

    // 使用匿名内部类实现工厂方法，更优雅
    // 一般只需要单一的工厂对象，所以声明为static
    public static ServiceFactory factory = () -> new Implementation1();
}

//class Implementation1Factory implements ServiceFactory{
//    @Override
//    public Service getService() {
//        return new Implementation1();
//    }
//}

class Implementation2 implements Service{
    @Override
    public void method1() {
        System.out.println("Implementation2 method1");
    }

    @Override
    public void method2() {
        System.out.println("Implementation2 method2");
    }

    public static ServiceFactory factory = () -> new Implementation2();
}

//class Implementation2Factory implements ServiceFactory{
//    @Override
//    public Service getService() {
//        return new Implementation2();
//    }
//}

public class Factories {

    public static void serviceConsumer(ServiceFactory fact){
        Service s = fact.getService();
        s.method1();
        s.method2();
    }

    public static void main(String args[]){
        // 使用工厂方法，可以实现透明替换，且不需要暴露出具体的接口
//        serviceConsumer(new Implementation1Factory());
//        serviceConsumer(new Implementation2Factory());
        serviceConsumer(Implementation1.factory);
        serviceConsumer(Implementation2.factory);
    }
}

