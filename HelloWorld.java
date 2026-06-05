/**
 * HelloWorld — Java 入门经典程序
 *
 * 功能：在控制台输出 "Hello, World!" 字符串
 * 目的：验证 Java 开发环境是否配置正确，演示 Java 程序的基本结构
 *
 * 编译方法：javac HelloWorld.java
 * 运行方法：java HelloWorld
 *
 * @author developer
 * @version 1.0
 * @since 2026-05-31
 */

/**
 * 主类名（HelloWorld）必须与文件名（HelloWorld.java）完全一致，
 * 包括大小写。这是 Java 编译器的强制要求。
 *
 * Java 程序的所有代码都必须定义在类（class）的内部。
 * public 表示这个类是公开的，可以被其他类访问。
 */
public class HelloWorld {

    /**
     * main 方法是 Java 应用程序的入口点（Entry Point）。
     * 当 JVM（Java 虚拟机）启动程序时，会从这里开始执行代码。
     *
     * 方法签名解析：
     *   public    — 访问修饰符，表示此方法可以被 JVM 外部调用
     *   static    — 静态方法，属于类本身而非类的实例，JVM 无需创建对象即可调用
     *   void      — 返回值类型，表示此方法不返回任何值
     *   main      — 方法名，JVM 约定入口方法必须叫 main
     *   String[] args — 命令行参数数组，允许用户在运行时传入参数
     *
     * @param args 命令行传入的字符串参数数组（可以通过 java HelloWorld arg1 arg2 传入）
     */
    public static void main(String[] args) {

        /*
         * System.out.println() 是 Java 中最常用的控制台输出方法。
         *
         * System     — java.lang 包中的一个最终类，提供系统相关的功能
         * .out       — System 类的一个静态成员变量，类型为 PrintStream，
         *              代表"标准输出流"（默认指向控制台/终端）
         * .println() — PrintStream 类的方法，打印字符串并在末尾自动换行
         *              相当于 print(str + "\n")
         *
         * 使用双引号 " 括起来的内容是字符串字面量（String literal）。
         */
        System.out.println("Hello, World!");

        /*
         * 除了 println()，System.out 还提供了其他输出方法：
         *   System.out.print("内容");     — 输出但不换行
         *   System.out.printf("格式化 %s", "值"); — 格式化输出（类似 C 语言的 printf）
         *
         * 下面的代码演示了如何打印多行信息（取消注释即可运行）：
         */
        // System.out.println("欢迎来到 Java 世界！🚀");
        // System.out.printf("当前 Java 版本：%s%n", System.getProperty("java.version"));

        /*
         * 演示如何读取命令行参数（如果传入了参数的话）：
         *   args.length   — 获取参数个数
         *   args[0]       — 第一个参数（索引从 0 开始）
         */
        if (args.length > 0) {
            System.out.println("您传入的命令行参数：");
            // 增强型 for 循环（for-each），遍历 args 数组中的所有元素
            for (int i = 0; i < args.length; i++) {
                System.out.println("  参数[" + i + "]: " + args[i]);
            }
        }

        /*
         * 程序执行到这里就结束了。
         * main 方法执行完毕后，JVM 进程会自动退出并返回退出码 0（表示正常结束）。
         * 如果程序出错退出，可以使用 System.exit(非零值) 返回错误码。
         */
    }
}
