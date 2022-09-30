package dk.kvalitetsit.ihexdsapi.dao;

import dk.kvalitetsit.ihexdsapi.dao.entity.LogEntry;
import dk.kvalitetsit.ihexdsapi.dao.impl.CacheRequestResponseHandleImpl;
import org.junit.*;


public class CacheRequestResponseHandleTest extends AbstractRedisTest {
    @Test
    public void test (){}


    private CacheRequestResponseHandleImpl subject;



    public String id;

    public LogEntry data;

    @Before
    public void setup() {
        subject = new CacheRequestResponseHandleImpl(redisTemplate);
        data = new LogEntry("Some ID", "Test");
        id = "TEST";
    }

    @After
    public void cleanup() {
        this.redisTemplate.opsForValue().getAndDelete(id);
    }



    @Test
    public void testGetRequestAndResponse() {
        System.out.println();
        Assert.assertNull(this.redisTemplate.opsForValue().get(id));
        this.redisTemplate.opsForValue().set(id, data);


        Assert.assertEquals(data.getPayload(), subject.getRequestAndResponse(id));
    }

    @Test
    public void saveRequestAndResponse() {
        Assert.assertNull(this.redisTemplate.opsForValue().get(id));

        subject.saveRequestAndResponse(id, data);

        Assert.assertEquals(data.getPayload(), subject.getRequestAndResponse(id));
  }
}
