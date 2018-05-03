package com.crud.tasks.trello.client;

import com.crud.tasks.domain.TrelloBoard;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloList;
import com.crud.tasks.domain.TrelloListDto;
import com.crud.tasks.mapper.TrelloMapper;
import com.crud.tasks.service.TrelloService;
import com.crud.tasks.trello.facade.TrelloFacade;
import com.crud.tasks.trello.validator.TrelloValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrelloFacadeTest {

    @InjectMocks
    TrelloFacade trelloFacade;

    @Mock
    private TrelloService trelloService;

    @Mock
    private TrelloMapper trelloMapper;

    @Mock
    private TrelloValidator trelloValidator;

    @Test
    public void shouldFetchEmptyList() {
        //given

        List<TrelloListDto> trelloLists = new ArrayList<>();
        trelloLists.add(new TrelloListDto("1", "test_list", false));

        List<TrelloBoardDto> trelloBoards = new ArrayList<>();
        trelloBoards.add(new TrelloBoardDto("1", "test", trelloLists));

        List<TrelloList> mappedtrelloLists = new ArrayList<>();
        mappedtrelloLists.add(new TrelloList("1", "test_list", false));

        List<TrelloBoard> mappedtrelloBoards = new ArrayList<>();
        mappedtrelloBoards.add(new TrelloBoard("1", "test", mappedtrelloLists));

        //when
        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoards);
        when(trelloMapper.mapToBoards(trelloBoards)).thenReturn(mappedtrelloBoards);
        when(trelloMapper.mapToBoardsDto(anyList())).thenReturn(new ArrayList<>());
        when(trelloValidator.validateTrelloBoards(mappedtrelloBoards)).thenReturn(new ArrayList<>());

        //then

        List<TrelloBoardDto> trelloBoardDtos = trelloFacade.fetchTrelloBoards();

        //when
        assertNotNull(trelloBoardDtos);
        assertEquals(0, trelloBoardDtos.size());
    }

    @Test
    public void shouldFetchTrelloBoards() {
        //given

        List<TrelloListDto> trelloLists = new ArrayList<>();
        trelloLists.add(new TrelloListDto("1", "test_list", false));

        List<TrelloBoardDto> trelloBoards = new ArrayList<>();
        trelloBoards.add(new TrelloBoardDto("1", "test", trelloLists));

        List<TrelloList> mappedtrelloLists = new ArrayList<>();
        mappedtrelloLists.add(new TrelloList("1", "test_list", false));

        List<TrelloBoard> mappedtrelloBoards = new ArrayList<>();
        mappedtrelloBoards.add(new TrelloBoard("1", "test", mappedtrelloLists));

        //when
        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoards);
        when(trelloMapper.mapToBoards(trelloBoards)).thenReturn(mappedtrelloBoards);
        when(trelloMapper.mapToBoardsDto(anyList())).thenReturn(trelloBoards);
        when(trelloValidator.validateTrelloBoards(mappedtrelloBoards)).thenReturn(mappedtrelloBoards);

        List<TrelloBoardDto> trelloBoardDtos = trelloFacade.fetchTrelloBoards();
        //then
        assertNotNull(trelloBoardDtos);
        assertEquals(1, trelloBoardDtos.size());

        trelloBoardDtos.forEach(trelloBoardDto -> {
            assertEquals("1", trelloBoardDto.getId());
            assertEquals("test", trelloBoardDto.getName());
            trelloBoardDto.getLists().forEach(trelloListDto -> {
                assertEquals("1", trelloListDto.getId());
                assertEquals("test_list", trelloListDto.getName());
                assertFalse(trelloListDto.isClosed());
            });
        });
    }
}

