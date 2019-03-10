package com.sbiitju.smartremainder.ClassSave;

public class SaveClass {
    FirstClass firstClass=new FirstClass();
    SecondClass secondClass=new SecondClass();
    ThirdClass thirdClass=new ThirdClass();
    ForthClass forthClass=new ForthClass();
    FIfthClass fIfthClass=new FIfthClass();

    public SaveClass(FirstClass firstClass, SecondClass secondClass, ThirdClass thirdClass, ForthClass forthClass, FIfthClass fIfthClass) {
        this.firstClass = firstClass;
        this.secondClass = secondClass;
        this.thirdClass = thirdClass;
        this.forthClass = forthClass;
        this.fIfthClass = fIfthClass;
    }

    public SaveClass() {
    }

    @Override
    public String toString() {
        return "SaveClass{" +
                "firstClass=" + firstClass +
                ", secondClass=" + secondClass +
                ", thirdClass=" + thirdClass +
                ", forthClass=" + forthClass +
                ", fIfthClass=" + fIfthClass +
                '}';
    }
}
