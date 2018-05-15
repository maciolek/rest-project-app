package com.crud.tasks.trello.client;

import com.crud.tasks.domain.*;
import com.crud.tasks.trello.config.TrelloConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TrelloClientTest {

    @InjectMocks
    TrelloClient trelloClient;

    @Mock
    RestTemplate restTemplate;

    @Mock
    TrelloConfig trelloConfig;

    @Before
    public void init() {
        when(trelloConfig.getTrelloApiEndpoint()).thenReturn("http://test.com");
        when(trelloConfig.getTrelloAppKey()).thenReturn("test");
        when(trelloConfig.getTrelloAppToken()).thenReturn("test");
        when(trelloConfig.getTrelloUsername()).thenReturn("maciolek.dariusz@gmail.com");
    }

    @Test
    public void shouldFetchTrelloBoards() throws URISyntaxException {

        //given
        TrelloBoardDto[] trelloBoards = new TrelloBoardDto[1];
        trelloBoards[0] = new TrelloBoardDto("test_id", "test_board", new ArrayList<>());

        URI uri = new URI("http://test.com/members/maciolek.dariusz@gmail.com/boards?key=test&token=test&fields=name,id&lists=all");

        when(restTemplate.getForObject(uri, TrelloBoardDto[].class)).thenReturn(trelloBoards);

        //when
        List<TrelloBoardDto> fetchedTrelloBoards = trelloClient.getTrelloBoards();

        //then
        assertEquals(1, fetchedTrelloBoards.size());
        assertEquals("test_id", fetchedTrelloBoards.get(0).getId());
        assertEquals("test_board", fetchedTrelloBoards.get(0).getName());
        assertEquals(new ArrayList<>(), fetchedTrelloBoards.get(0).getLists());
    }

    @Test
    public void shouldFetchEmptyTrelloBoards() throws URISyntaxException {

        //given
        TrelloBoardDto[] trelloBoards = new TrelloBoardDto[1];
        trelloBoards[0] = new TrelloBoardDto("test_id", "test_board", new ArrayList<>());

        URI uri = new URI("http://test.com/members/maciolek.dariusz@gmail.com/boards?key=test&token=test&fields=name,id&lists=all");

        // when
        when(trelloClient.getTrelloBoards()).thenThrow(RestClientException.class).thenReturn(new ArrayList<>());
        List<TrelloBoardDto> fetchedTrelloBoards = trelloClient.getTrelloBoards();

        //then
        Assert.assertNotNull(fetchedTrelloBoards);
        Assert.assertTrue(fetchedTrelloBoards.isEmpty());
    }

    @Ignore
    @Test
    public void shouldCreateCard() throws URISyntaxException {
        //given
        TrelloCardDto trelloCardDto = new TrelloCardDto(
                "Test task",
                "Test Description",
                "top",
                "test_id");
        //when
        URI uri = new URI("http://test.com/cards?key=test&token=test&name=Test%20task&desc=Test%20Description&pos=top&idList=test_id");

        CreatedTrelloCardDto createdTrelloCardDto = new CreatedTrelloCardDto(
                "1",
                "Test task",
                "http://test.com",
                new Badges(1,
                        new AttachmentsByType(
                                new Trello(11, 22)
                        ))
        );

        when(restTemplate.postForObject(uri, null, CreatedTrelloCardDto.class)).thenReturn(createdTrelloCardDto);

        //then

        CreatedTrelloCardDto newCard = trelloClient.createNewCard(trelloCardDto);

        assertEquals("1", newCard.getId());
        assertEquals("Test task", newCard.getName());
    }
}