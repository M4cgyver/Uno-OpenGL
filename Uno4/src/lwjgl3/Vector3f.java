package lwjgl3;
 
public class Vector3f
{
	public float x = 0,y = 0,z = 0;
	
	public Vector3f(){}
	
	public Vector3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(Vector3f v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public static Vector3f GRAVITY = new Vector3f(0,-9.81f, 0);
	
	public Vector3f dupe()
	{
		return new Vector3f(this);
	}
	
	public void clear()
	{
		this.x = this.y = this.z = 0;
	}
	
	public Vector3f(Number x, Number y, Number z)
	{
		this.x = x.floatValue();
		this.y = y.floatValue();
		this.z = z.floatValue();
	}
	
	public void add(Vector3f v)
	{
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	public void add(Number n)
	{
		x += n.floatValue();
		y += n.floatValue();
		z += n.floatValue();
	}
	
	public void sub(Vector3f v)
	{
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}
	
	public void sub(Number n)
	{
		x -= n.floatValue();
		y -= n.floatValue();
		z -= n.floatValue();
	}
	
	public void mul(Vector3f v)
	{
		x *= v.x;
		y *= v.y;
		z *= v.z;
	}
	
	public void mul(Number n)
	{
		x *= n.floatValue();
		y *= n.floatValue();
		z *= n.floatValue();
	}
	
	public float mulf(Vector3f vector)
	{
		return x*vector.x + y*vector.y + z*vector.z;
	}
	
	public void div(Vector3f v)
	{
		x /= v.x;
		y /= v.y;
		z /= v.z;
	}
	
	public void div(Number n)
	{
		x /= n.floatValue();
		y /= n.floatValue();
		z /= n.floatValue();
	}
	
	public static Vector3f add(Vector3f v1, Vector3f v2)
	{
		return new Vector3f(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}
	
	public static Vector3f add(Vector3f v1, Number n)
	{
		return new Vector3f(v1.x + n.floatValue(), v1.y + n.floatValue(), v1.z + n.floatValue());
	}
	
	public static Vector3f sub(Vector3f v1, Vector3f v2)
	{
		return new Vector3f(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}
	
	public static Vector3f sub(Vector3f v1, Number n)
	{
		return new Vector3f(v1.x - n.floatValue(), v1.y - n.floatValue(), v1.z - n.floatValue());
	}
	
	public static Vector3f mul(Vector3f v1, Vector3f v2)
	{
		return new Vector3f(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
	}
	
	public static Vector3f mul(Vector3f v1, Number n)
	{
		return new Vector3f(v1.x * n.floatValue(), v1.y * n.floatValue(), v1.z * n.floatValue());
	}
	
	public static Vector3f div(Vector3f v1, Vector3f v2)
	{
		return new Vector3f(v1.x / v2.x, v1.y / v2.y, v1.z / v2.z);
	}
	
	public static Vector3f div(Vector3f v1, Number n)
	{
		return new Vector3f(v1.x / n.floatValue(), v1.y / n.floatValue(), v1.z / n.floatValue());
	}
	
	public void addScaledVector(Vector3f vector, Number scale)
	{
		x += vector.x * scale.floatValue();
		y += vector.y * scale.floatValue();
		z += vector.z * scale.floatValue();
	}
	
	public Vector3f componentProduct(Vector3f vector) 
	{
		return new Vector3f(x * vector.x, y * vector.y, z * vector.z);
	}
	
	public float scalarProduct(Vector3f vector)
	{
		return x*vector.x + y*vector.y + z*vector.z;
	}
	
	public Vector3f vectorProduct(Vector3f vector)
	{
		return new Vector3f(	y*vector.z-z*vector.y,
							z*vector.x-x*vector.z,
							x*vector.y-y*vector.x);
	}
	
	public float magnitude()
	{
        return (float)Math.sqrt((double)(x*x+y*y+z*z));
	}
    
	/** Turns a non-zero vector into a vector of unit length. */
    public void normalize()
    {
        float l = magnitude();
        if (l > 0)
        {
            mul((1.0f)/l);
        }
    }
    
	public void prod(Vector3f vector)
	{	
		Vector3f p = vectorProduct(vector);
		this.x = p.x; this.y = p.y; this.z = p.z;
	}

	public static Vector3f random(Vector3f minVelocity, Vector3f maxVelocity) {
		return new Vector3f(minVelocity.x + Math.random() * (maxVelocity.x - minVelocity.x), minVelocity.y + Math.random() * (maxVelocity.y - minVelocity.y), minVelocity.z + Math.random() * (maxVelocity.z - minVelocity.z));
	}
 
}