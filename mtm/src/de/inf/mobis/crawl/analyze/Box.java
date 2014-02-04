package de.inf.mobis.crawl.analyze;

public final class Box
{
    public int x1;
    public int x2;
    public int y1;
    public int y2;
    
    private Box()
    {
    }
    
    public static Box get(int x, int y, int x2, int y2)
    {
        Box b = new Box();
        b.x1 = x;
        b.x2 = x2;
        b.y1 = y;
        b.y2 = y2;
        return b;
    }

    public int distance(Box b, boolean upperLeft)
    {
        if (upperLeft)
        {
            return (int) Math.sqrt(Math.pow(x1 - b.x1, 2) + Math.pow(y1 - b.y1, 2));
        }
        else
        {
            return (int) Math.sqrt(Math.pow(x2 - b.x2, 2) + Math.pow(y2 - b.y2, 2));
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Box)
        {
            Box box = (Box) obj;
            return (box.x1 == x1 && box.x2 == x2 && box.y1 == y1 && box.y2 == y2);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return x1 + x2 + y1 + y2;
    }


    @Override
    public String toString()
    {
        return "Box [x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + "]";
    }

}