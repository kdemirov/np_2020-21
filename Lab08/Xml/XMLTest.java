package Lab08.Xml;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface XMLComponent {
    void addAttribute(String type, String value);

    void addComponent(XMLComponent component);

    void print(int indent);
}

class XMLLeaf implements XMLComponent {
    String tag;
    String value;
    Map<String, String> attributes;

    public XMLLeaf(String tag, String value) {
        this.tag = tag;
        this.value = value;
        this.attributes = new LinkedHashMap<>();
    }


    @Override
    public void addAttribute(String type, String value) {
        this.attributes.putIfAbsent(type, value);
    }

    @Override
    public void addComponent(XMLComponent component) {

    }

    @Override
    public void print(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("\t");
        }
        sb.append(String.format("<%s ", this.tag));
        this.attributes.entrySet().forEach(entry -> sb.append(String.format("%s=%c%s%c ", entry.getKey(), '"', entry.getValue(), '"')));
        sb.deleteCharAt(sb.length() - 1);
        sb.append(">").append(String.format("%s</%s>", this.value, this.tag));
        System.out.println(sb.toString());

    }


}

class XMLComposite implements XMLComponent {
    String name;
    Map<String, String> attributes;
    List<XMLComponent> components;

    public XMLComposite(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.attributes = new LinkedHashMap<>();

    }

    @Override
    public void addAttribute(String type, String value) {
        this.attributes.putIfAbsent(type, value);
    }

    @Override
    public void addComponent(XMLComponent component) {
        this.components.add(component);
    }

    @Override
    public void print(int indent) {
        StringBuilder sbBegin = new StringBuilder();
        StringBuilder sbEnd = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sbBegin.append("\t");
            sbEnd.append("\t");
        }
        sbBegin.append(String.format("<%s ", name));
        this.attributes.entrySet().forEach(entry -> sbBegin.append(String.format("%s=%c%s%c ", entry.getKey(), '"', entry.getValue(), '"')));
        sbBegin.deleteCharAt(sbBegin.length() - 1).append(">");
        System.out.println(sbBegin.toString());
        for (XMLComponent component : components) {
            component.print(indent + 1);
        }
        sbEnd.append(String.format("</%s>", name));
        System.out.println(sbEnd.toString());
    }
}

public class XMLTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        XMLComponent component = new XMLLeaf("student", "Trajce Trajkovski");
        component.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        XMLComposite composite = new XMLComposite("name");
        composite.addComponent(new XMLLeaf("first-name", "trajce"));
        composite.addComponent(new XMLLeaf("last-name", "trajkovski"));
        composite.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        if (testCase == 1) {
            //TODO Print the component object
            component.print(0);
        } else if (testCase == 2) {
            //TODO print the composite object
            composite.print(0);
        } else if (testCase == 3) {
            XMLComposite main = new XMLComposite("level1");
            main.addAttribute("level", "1");
            XMLComposite lvl2 = new XMLComposite("level2");
            lvl2.addAttribute("level", "2");
            XMLComposite lvl3 = new XMLComposite("level3");
            lvl3.addAttribute("level", "3");
            lvl3.addComponent(component);
            lvl2.addComponent(lvl3);
            lvl2.addComponent(composite);
            lvl2.addComponent(new XMLLeaf("something", "blabla"));
            main.addComponent(lvl2);
            main.addComponent(new XMLLeaf("course", "napredno programiranje"));

            //TODO print the main object
            main.print(0);
        }
    }
}
