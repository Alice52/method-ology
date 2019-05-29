## 相关认知

### 1.使用 `Lambda` 表达式

- demo

```java
Comparator<Integer> comparator = new Comparator<Integer>() {
    @Override
    public int compare(Integer o1, Integer o2) {
        return Integer.compare(o1, o2);
    }
};
```

1.  ==> 优化

```java
TreeSet<Integer> treeSet = new TreeSet<>((o1, o2) -> Integer.compare(o1, o2));
```

### 2.当出现大量重复的代码时, 可以想象以下使用 `设计模式` 进行优化

- 需求
  - 按照年纪过滤员工
  - 按照工资过滤员工
- demo

```java
/**
* function: 过滤年纪大于 20 岁的员工
* @param employees
* @return List<Employee>
*/
public List<Employee> filterEmployeeByAge(List<Employee> employees) {
    List<Employee> emps = new ArrayList<>();
    for (Employee emp : employees) {
        if (emp.getAge() > 20) emps.add(emp);
    }
    return emps;
}

/**
* function: 过滤工资大于 5000 的员工
* @param employees
* @return List<Employee>
*/
public List<Employee> filterEmployeeBySalary(List<Employee> employees) {
    List<Employee> emps = new ArrayList<>();
    Iterator iterator = employees.iterator();
    while (iterator.hasNext()) {
        Employee employee = (Employee) iterator.next();
        if (employee.getSalary() > 5000) emps.add(employee);
    }
    return emps;
}

@Test
public void testFilterEmployee() {
    List<Employee> emps = filterEmployeeByAge(employees);
    List<Employee> emps2 = filterEmployeeBySalary(employees);
    System.out.println(emps2);
}
```

1. ==> 使用 `策略设计模式` 和 `Lambda` 进行优化

- 按照需求抽象接口: 过滤员工

```java
@FunctionalInterface
public interface FilterInterface<T>{
    public boolean test(T t);
}
```

- 使用接口实现需求

```java
 // 优化1: 使用 设计模式进行优化: 策略设计模式-接口
public List<Employee> filterEmployee(List<Employee> employees, FilterInterface<Employee> fe){
    List<Employee> emps = new ArrayList<>();
    for (Employee employee : employees) {
        if (fe.test(employee)) emps.add(employee);
    }
    return emps;
}
@Test
public void testFilterEmployee3() {
    // 过滤工资大于 5000 的员工
    List<Employee> emps = filterEmployee(employees, new FilterInterface<Employee>(){
        @Override
        public boolean test(Employee employee) {
            return employee.getSalary() > 5000;
        }
    });
    System.out.println(emps);
    // 过滤年纪大于 30 的员工
    List<Employee> emps2 = filterEmployee(employees, (employee) -> employee.getAge() > 30);
    emps2.forEach(System.out::println);
}
```

2. ==> 使用 stream 进行优化

```java
// 优化2: 使用 Stream 进行优化
@Test
public void testFilterEmployee4() {
    employees.stream().filter((e)-> e.getSalary() > 5000)
            .forEach(System.out::println);
}
```
